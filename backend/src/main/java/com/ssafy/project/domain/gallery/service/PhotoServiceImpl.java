package com.ssafy.project.domain.gallery.service;


import com.drew.lang.GeoLocation;
import com.ssafy.project.domain.gallery.dto.internal.*;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;
import com.ssafy.project.domain.gallery.repository.PhotoRepository;
import com.ssafy.project.util.ExifUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
	private final KakaoGeocodingService kakaoGeocodingService;
	private final PhotoRepository photoRepository;
    private final MinioService minioService;
	
	// [upload]
	public PhotoDto uploadPhoto(MultipartFile file, String s3Key){
        String filename = Paths.get(s3Key).getFileName().toString();  // ✅ 파일명만 추출
        // ✅ 사진 객체 생성
        PhotoDto photo = new PhotoDto();
        photo.setS3Key(s3Key);
        photo.setFilename(filename);
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
    public void uploadPhotos(List<UploadDto> uploadList){
        List<PhotoDto> photos = new ArrayList<>();
        for(UploadDto uploadDto : uploadList){
            photos.add(uploadPhoto(uploadDto.getFile(), uploadDto.getKey()));
        }
        photoRepository.insertPhotos(photos);
    }
	
	// [rename]
    @Override
    public void renamePhoto(String oldKey, String newKey) {
        String filename = Paths.get(newKey).getFileName().toString();
        photoRepository.renamePhoto(oldKey, newKey, filename);
    }

    @Override
    public void renamePhotos(List<S3KeyUpdateDto> renameList) {
        for(S3KeyUpdateDto key : renameList){
            renamePhoto(key.getOldKey(), key.getNewKey());
        }
    }

    @Override
    public PhotoDetailResponseDto getDetailPhoto(String key) {
        return photoRepository.findPhotoDetailByS3Key(key);
    }


    // [list]
    @Override
    public DirectoryResponseDto listDirectory(String prefix){
        List<DirectoryDto> directories = photoRepository.findDirectoriesByPrefix(prefix);
        List<FileDto> files = photoRepository.findFilesByPrefix(prefix);
        for(FileDto file : files){
            file.setPresignedUrl(minioService.generatePresignedUrl(file.getS3Key()));
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
}
