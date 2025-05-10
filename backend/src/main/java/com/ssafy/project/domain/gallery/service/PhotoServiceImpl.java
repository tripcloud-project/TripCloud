package com.ssafy.project.domain.gallery.service;


import com.drew.lang.GeoLocation;
import com.ssafy.project.domain.gallery.dto.internal.*;
import com.ssafy.project.domain.gallery.dto.request.RenameRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.repository.PhotoRepository;
import com.ssafy.project.infra.kakao.KakaoGeocodingService;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.ExifUtil;
import com.ssafy.project.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
	private final KakaoGeocodingService kakaoGeocodingService;
	private final PhotoRepository photoRepository;
    private final S3Service s3Service;
	
	// [upload]
	public PhotoDto uploadPhoto(UploadDto uploadDto){
		MultipartFile file = uploadDto.getFile();
		String s3Key = uploadDto.getS3Key();
		String originalFileName = uploadDto.getOriginalFilename();
        // ✅ 사진 객체 생성
        PhotoDto photo = new PhotoDto();
        photo.setS3Key(s3Key);
        photo.setOriginalFilename(originalFileName);
        photo.setSize(file.getSize());
        photo.setContentType(file.getContentType());

        // ✅ 위치 정보 로그
        GeoLocation location = ExifUtil.extractGps(file);
        if (location != null) {
            photo.setLatitude(location.getLatitude());
            photo.setLongitude(location.getLongitude());

            AddressDto address = kakaoGeocodingService.reverseGeocode(location.getLatitude(), location.getLongitude());
            if (address != null) {
                photo.setSido(address.getSido());
                photo.setSigungu(address.getSigungu());
                photo.setEupmyeondong(address.getEupmyeondong());
            }
        }

        // ✅ 사진 촬영일 추가
        LocalDateTime takenAt = ExifUtil.extractDateTaken(file);
        if(takenAt != null){
            photo.setTakenAt(takenAt);
        }
        return photo;
    }
	
	@Override
    public void uploadPhotos(List<MultipartFile> files, String prefix){
		prefix = makeMemberPrefix(prefix);
		if (files == null || files.isEmpty()) {
            throw new UploadFailException("빈 파일 업로드 에러");
        }
		List<PhotoDto> photos = new ArrayList<>();
		
		// s3에 업로드된 list
		List<UploadDto> uploadList = s3Service.uploadObjects(files, prefix);
		
        for(UploadDto uploadDto : uploadList){
            photos.add(uploadPhoto(uploadDto));
        }
        Long memberId = SecurityUtil.getCurrentMemberId();
        photoRepository.insertPhotos(photos, memberId);

    }
	
	// [rename]
	@Override
	public void renameObjects(RenameRequestDto renameRequestDto) {
		String newKey = makeMemberPrefix(renameRequestDto.getNewKey());
		
		// isDirectory?
		if(renameRequestDto.getPhotoId() == null) {
			String oldKey = makeMemberPrefix(renameRequestDto.getOldKey()); 
			s3Service.directoryKeyUpdate(oldKey, newKey);
			renamePhotos(oldKey, newKey);
		}else {
			Long photoId = renameRequestDto.getPhotoId();
			renamePhoto(photoId, newKey);
		}
	}
	
	// 파일 이름 수정
    private void renamePhoto(Long photoId, String newKey) {
        String filename = Paths.get(newKey).getFileName().toString();
        Long memberId = SecurityUtil.getCurrentMemberId();
        photoRepository.renamePhoto(photoId, filename, memberId);
    }

    
    // 디렉토리 이름 수정
    // TODO: [rename] 디렉토리 이름 중복은 불가능하게 막아야합니다.
    private void renamePhotos(String oldKey, String newKey) {
    	photoRepository.renamePhotos(oldKey, newKey);
    }

    @Override
    public PhotoDetailResponseDto getDetailPhoto(Long photoId) {
    	Long memberId = SecurityUtil.getCurrentMemberId();
        return photoRepository.findPhotoDetailByPhotoIdAndMemberId(photoId, memberId);
    }


    // [list]
    @Override
    public DirectoryResponseDto listDirectory(String prefix){
    	prefix = makeMemberPrefix(prefix);
        List<DirectoryDto> directories = photoRepository.findDirectoriesByPrefix(prefix);
        List<FileDto> files = photoRepository.findFilesByPrefix(prefix);
        for(FileDto file : files){
            file.setPresignedUrl(s3Service.generatePresignedUrl(file.getS3Key()));
            file.setS3Key(null);
        }

        // prefix 앞에 사용자 email 제거해서 반환
        String currentPrefix = prefix.contains("/") ? prefix.substring(prefix.indexOf('/') + 1) : "";

        DirectoryResponseDto directoryResponseDto = DirectoryResponseDto.builder()
                .prefix(currentPrefix)
                .directories(directories)
                .files(files)
                .totalSize(
                        directories.stream().mapToLong(d -> d.getSize() != null ? d.getSize() : 0L).sum() +
                                files.stream().mapToLong(f -> f.getSize() != null ? f.getSize() : 0L).sum()
                )
                .build();
        return directoryResponseDto;
    }

    // [download]
    @Override
    public DownloadDto downloadPhoto(Long photoId){
        Long memberId = SecurityUtil.getCurrentMemberId();
        S3KeyOriginalFilenameDto s3KeyOriginalFilenameDto = photoRepository.findS3KeyAndOriginalFilenameByPhotoIdAndMemberId(photoId, memberId);
        Resource resource = s3Service.createResource(s3KeyOriginalFilenameDto.getS3Key());

        return DownloadDto.builder()
                .resource(resource)
                .contentDisposition(makeContentDisposition(s3KeyOriginalFilenameDto.getOriginalFilename()))
                .build();
    }

    @Override
    public DownloadDto downloadDirectory(String prefix){
        prefix = makeMemberPrefix(prefix);
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<S3KeyOriginalFilenameDto> s3KeyOriginalFilenames = photoRepository.findS3KeysAndOriginalFilenamesByPrefixAndMemberId(prefix, memberId);
        Resource zipResource = s3Service.createZipResource(prefix, s3KeyOriginalFilenames);
        String zipFilename = getZipFilenameFromKey(prefix);

        return DownloadDto.builder()
                .resource(zipResource)
                .contentDisposition(makeContentDisposition(zipFilename))
                .build();
    }

    private String getZipFilenameFromKey(String prefix) {
        String dirName = prefix;
        if (dirName.endsWith("/")) {
            dirName = dirName.substring(0, dirName.length() - 1);
        }
        dirName = dirName.substring(dirName.lastIndexOf('/') + 1);
        return dirName + ".zip";
    }

    // Content-Disposition 헤더를 UTF-8으로 설정하여 파일명에 한글, 공백이 있을 경우에도 올바르게 처리
    private String makeContentDisposition(String filename){
        return ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build()
                .toString();
    }
    
	private String makeMemberPrefix(String prefix) {
		String email = SecurityUtil.getCurrentMemberEmail();
        return String.format("%s/%s", email, prefix.replaceAll("^/+", ""));
	}
}
