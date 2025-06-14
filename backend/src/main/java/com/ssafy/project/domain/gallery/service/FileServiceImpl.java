package com.ssafy.project.domain.gallery.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.ssafy.project.domain.gallery.dto.internal.*;
import com.ssafy.project.domain.member.service.MemberService;
import com.ssafy.project.util.AesUtil;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drew.lang.GeoLocation;
import com.ssafy.project.domain.gallery.dto.request.DeleteRequestDto;
import com.ssafy.project.domain.gallery.dto.request.DirectoryRenameRequestDto;
import com.ssafy.project.domain.gallery.dto.request.DownloadRequestDto;
import com.ssafy.project.domain.gallery.dto.request.RestoreRequestDto;
import com.ssafy.project.domain.gallery.dto.request.TrashRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryPreviewResponseDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FilePreviewResponseDto;
import com.ssafy.project.domain.gallery.dto.response.SearchResultResponseDto;
import com.ssafy.project.domain.gallery.exception.RenameFailException;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.repository.FileRepository;
import com.ssafy.project.domain.gallery.service.helper.DownloadHelper;
import com.ssafy.project.domain.gallery.service.helper.UploadHelper;
import com.ssafy.project.infra.kakao.KakaoGeocodingService;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.ExifUtil;
import com.ssafy.project.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final KakaoGeocodingService kakaoGeocodingService;
    private final FileRepository fileRepository;
    private final S3Service s3Service;
    private final HashtagService hashtagService;
    private final AesUtil aesUtil;
    private final PhotoService photoService;
    private final MemberService memberService;

    // [/upload]
    @Override
    public void uploadFiles(List<MultipartFile> files, String prefix) {
        prefix = makeMemberPrefix(prefix);
        if (files == null || files.isEmpty()) {
            throw new UploadFailException("빈 파일 업로드 에러");
        }
        List<UploadDto> uploadList = new ArrayList<>();
        Map<String, List<MultipartFile>> grouped = UploadHelper.groupFilesByDirectory(files);

        for (Map.Entry<String, List<MultipartFile>> entry : grouped.entrySet()) {
            String dirName = entry.getKey();
            List<MultipartFile> fileGroup = entry.getValue();
            if (dirName.isEmpty()) {
                uploadList.addAll(uploadOnlyFiles(fileGroup, prefix)); // 디렉토리 없이, 파일들만 업로드
            } else {
                uploadList.addAll(uploadDirectoryFiles(fileGroup, prefix)); // 디렉토리 단위로 업로드
            }
        }

        // S3에 업로드 성공한 파일들만 모아서 Photo 객체를 생성합니다.
        List<FileDto> fileList = new ArrayList<>();
        for (UploadDto uploadDto : uploadList) {
            fileList.add(makeFile(uploadDto));
        }
        Long memberId = SecurityUtil.getCurrentMemberId();
        fileRepository.insertFiles(fileList, memberId);

        // 멤버의 usedStorage를 업데이트합니다.
        memberService.updateStorageByMemberId(memberId);

        // 썸네일에 대표 이미지가 없다면, 첫 번째 사진을 추가합니다.
        List<ThumbnailDto> existingThumbnails = fileRepository.findThumbnailsByMemberId(memberId);
        System.out.println(existingThumbnails);
        Set<String> thumbnailKeySet = existingThumbnails.stream()
                .map(t -> t.getSido() + "::" + (t.getSigungu() != null ? t.getSigungu() : ""))
                .collect(Collectors.toSet());

        for(FileDto file : fileList){
            if(file.getSido() == null || file.getSigungu() == null) continue;

            String sidoKey = file.getSido() + "::";
            String sigunguKey = file.getSido() + "::" + file.getSigungu();

            // 썸네일이 없으면 이 파일을 썸네일로 설정
            if (!thumbnailKeySet.contains(sidoKey)) {
                Long fileId = fileRepository.findFileIdByS3KeyAndMemberId(file.getS3Key(), memberId);
                photoService.setThumbnail(fileId, "sido");
                thumbnailKeySet.add(sidoKey);
            }
            if (!thumbnailKeySet.contains(sigunguKey)) {
                Long fileId = fileRepository.findFileIdByS3KeyAndMemberId(file.getS3Key(), memberId);
                photoService.setThumbnail(fileId, "sigungu");
                thumbnailKeySet.add(sigunguKey);
            }
        }


        // 비동기 AI 호출을 위한 코드
        // upload 응답 이후에 사라질 file을 byte로 저장해 둡니다.
        List<ByteFileDto> byteFileList = new ArrayList<>();
        for (UploadDto uploadDto : uploadList) {
            try {
                byteFileList.add(
                        ByteFileDto.builder()
                                .s3Key(uploadDto.getS3Key())
                                .contentType(uploadDto.getFile().getContentType())
                                .content(uploadDto.getFile().getBytes())
                                .build()
                );
            } catch (IOException e) {
                log.error("byte 파싱 실패는 skip합니다.");
            }
        }
        // 요청 응답 이후, 비동기 호출로 AI를 활용하여 해시태그를 파싱합니다.
        hashtagService.parseHashtag(byteFileList);
    }


    // 파일들만 업로드합니다. 중복 이름 걱정은 없습니다.
    private List<UploadDto> uploadOnlyFiles(List<MultipartFile> files, String prefix) {
        List<UploadDto> uploadList = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                if (file.isEmpty()) continue;
                String originalFilename = file.getOriginalFilename();
                String s3Key = UploadHelper.generateUuidS3Key(prefix + originalFilename);
                s3Service.uploadObject(file, s3Key);
                uploadList.add(new UploadDto(file, s3Key, originalFilename));
            } catch (IOException e) {
                log.error("파일 업로드 실패. 해당 파일은 skip합니다 : {}", file.getOriginalFilename(), e);
            }
        }
        return uploadList;
    }

    // 디렉토리 단위로 파일을 업로드합니다. 중복 이름을 고려해야합니다.
    private List<UploadDto> uploadDirectoryFiles(List<MultipartFile> files, String prefix) {
        List<UploadDto> uploadList = new ArrayList<>();
        String resolvedPrefix = resolveUniquePrefix(files, prefix);

        for (MultipartFile file : files) {
            try {
                if (file.isEmpty()) continue;
                String originalPath = file.getOriginalFilename();
                String originalFilename = Paths.get(originalPath).getFileName().toString();
                String relativePath = originalPath.substring(originalPath.indexOf("/") + 1);
                String s3Key = UploadHelper.generateUuidS3Key(resolvedPrefix + relativePath);
                s3Service.uploadObject(file, s3Key);
                uploadList.add(new UploadDto(file, s3Key, originalFilename));
            } catch (IOException e) {
                log.error("파일 업로드 실패. 해당 파일은 skip합니다 : {}", file.getOriginalFilename(), e);
            }
        }
        return uploadList;
    }

    // 동일한 이름의 디렉토리가 있을 경우, 이름 뒤에 (1)을 붙입니다.
    private String resolveUniquePrefix(List<MultipartFile> files, String prefix) {
        String firstDirName = files.stream()
                .map(MultipartFile::getOriginalFilename)
                .filter(name -> name != null && name.contains("/"))
                .map(name -> name.split("/")[0])
                .findFirst()
                .orElse("upload");

        String base = prefix + firstDirName;
        String resolvedPrefix = base + "/";
        int counter = 1;

        while (existsInS3(resolvedPrefix)) {
            resolvedPrefix = prefix + firstDirName + " (" + counter + ")/";
            counter++;
        }
        return resolvedPrefix;
    }

    // 동일한 directory가 이미 존재하는지 확인합니다.
    private boolean existsInS3(String prefix) {
        return fileRepository.existsByPrefix(prefix);
    }

    // 사진 객체를 생성합니다.
    private FileDto makeFile(UploadDto uploadDto) {
        MultipartFile file = uploadDto.getFile();
        String s3Key = uploadDto.getS3Key();
        String originalFileName = uploadDto.getOriginalFilename();
        // ✅ 사진 객체 생성
        FileDto fileDto = new FileDto();
        fileDto.setS3Key(s3Key);
        fileDto.setOriginalFilename(originalFileName);
        fileDto.setSize(file.getSize());
        fileDto.setContentType(file.getContentType());

        // ✅ 위치 정보 로그
        GeoLocation location = ExifUtil.extractGps(file);
        if (location != null) {
            fileDto.setLatitude(aesUtil.encrypt(location.getLatitude()));
            fileDto.setLongitude(aesUtil.encrypt(location.getLongitude()));

            AddressDto address = kakaoGeocodingService.reverseGeocode(location.getLatitude(), location.getLongitude());
            if (address != null) {
                fileDto.setSido(address.getSido());
                fileDto.setSigungu(address.getSigungu());
                fileDto.setEupmyeondong(address.getEupmyeondong());
            }
        }

        // ✅ 사진 촬영일 추가
        LocalDateTime takenAt = ExifUtil.extractDateTaken(file);
        if (takenAt != null) {
            fileDto.setTakenAt(takenAt);
        }
        return fileDto;
    }

    // 파일 이름 수정
    // [/rename/{fileId}]
    @Override
    public void renameFile(Long fileId, String filename) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        fileRepository.renameFile(fileId, filename, memberId);
    }

    // 디렉토리 이름 수정
    // [/rename]
    @Override
    public void renameDirectory(DirectoryRenameRequestDto directoryRenameRequestDto) {
        String oldPrefix = makeMemberPrefix(directoryRenameRequestDto.getOldPrefix());
        String newPrefix = makeMemberPrefix(directoryRenameRequestDto.getNewPrefix());
        if (existsInS3(newPrefix)) {
            throw new RenameFailException("동일한 이름이 디렉토리가 존재합니다.");
        }
        s3Service.directoryKeyUpdate(oldPrefix, newPrefix);
        Long memberId = SecurityUtil.getCurrentMemberId();
        fileRepository.renameFiles(oldPrefix, newPrefix, memberId);
    }

    // [/detail/{fileId}]
    @Override
    public FileDetailResponseDto getDetailFile(Long fileId) {
    	Long memberId = SecurityUtil.getCurrentMemberId();
    	FileDetailResponseDto detailResponseDto = fileRepository.findFileDetailByFileIdAndMemberId(fileId, memberId);

        // 복호화
        detailResponseDto.setLatitude(aesUtil.decrypt(detailResponseDto.getLatitude()));
        detailResponseDto.setLongitude(aesUtil.decrypt(detailResponseDto.getLongitude()));

        // 해시태그 추가
    	detailResponseDto.setHashtags(hashtagService.getHashtags(fileId));
        return detailResponseDto;
    }


    // [/list]
    @Override
    public DirectoryResponseDto listDirectory(String prefix, Boolean isDeleted) {
        prefix = makeMemberPrefix(prefix);
        List<DirectoryEntry> directories = fileRepository.findDirectoriesByPrefixAndIsDeleted(prefix, isDeleted);
        List<FileEntry> files = fileRepository.findFilesByPrefixAndIsDeleted(prefix, isDeleted);
        for (FileEntry file : files) {
        	if(file.getContentType().startsWith("image")) {
        		file.setPresignedUrl(s3Service.generateThumbnailPresignedUrl(file.getS3Key()));        		
        	}
            file.setS3Key(null);
            if(file.getLatitude()!=null && file.getLongitude()!=null){
                file.setLatitude(aesUtil.decrypt(file.getLatitude()));
                file.setLongitude(aesUtil.decrypt(file.getLongitude()));
            }

            // 해시태그 추가
            file.setHashtags(hashtagService.getHashtags(file.getFileId()));
        }

        // prefix 앞에 사용자 email 제거해서 반환
        String currentPrefix = prefix.contains("/") ? prefix.substring(prefix.indexOf('/') + 1) : "";

        return DirectoryResponseDto.builder()
                .prefix(currentPrefix)
                .directories(directories)
                .files(files)
                .totalSize(
                        directories.stream().mapToLong(d -> d.getSize() != null ? d.getSize() : 0L).sum() +
                                files.stream().mapToLong(f -> f.getSize() != null ? f.getSize() : 0L).sum()
                )
                .build();
    }

    // [/download]
    @Override
    public DownloadDto downloadBulk(DownloadRequestDto downloadRequestDto) {
        List<String> prefixList = downloadRequestDto.getPrefixList();
        List<Long> fileIdList = downloadRequestDto.getFileIdList();
        String currentPrefix = downloadRequestDto.getCurrentPrefix();
        
        // 단일 파일 다운로드. zip일 필요가 없는 경우
        if (prefixList.isEmpty() && fileIdList.size() == 1) {
            return downloadPhoto(fileIdList.get(0));
        } else {
            currentPrefix = makeMemberPrefix(currentPrefix);
            Long memberId = SecurityUtil.getCurrentMemberId();
            List<S3KeyOriginalFilenameDto> s3KeyOriginalFilenames = new ArrayList<>();
            for (String prefix : prefixList) {
                prefix = makeMemberPrefix(prefix);
                s3KeyOriginalFilenames.addAll(fileRepository.findS3KeysAndOriginalFilenamesByPrefixAndMemberId(prefix, memberId));
            }
            for (Long fileId : fileIdList) {
                s3KeyOriginalFilenames.add(fileRepository.findS3KeyAndOriginalFilenameByFileIdAndMemberId(fileId, memberId));
            }
            Resource zipResource = s3Service.createZipResource(currentPrefix, s3KeyOriginalFilenames);
            String zipFilename = DownloadHelper.getZipFilenameFromKey(currentPrefix);

            return DownloadDto.builder()
                    .resource(zipResource)
                    .contentDisposition(DownloadHelper.makeContentDisposition(zipFilename))
                    .build();
        }
    }

    // 단일 파일 zip 없이 다운로드
    private DownloadDto downloadPhoto(Long fileId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        S3KeyOriginalFilenameDto s3KeyOriginalFilenameDto = fileRepository.findS3KeyAndOriginalFilenameByFileIdAndMemberId(fileId, memberId);
        Resource resource = s3Service.createResource(s3KeyOriginalFilenameDto.getS3Key());

        return DownloadDto.builder()
                .resource(resource)
                .contentDisposition(DownloadHelper.makeContentDisposition(s3KeyOriginalFilenameDto.getOriginalFilename()))
                .build();
    }

    // [/trash]
    @Override
    public void trashBulk(TrashRequestDto trashRequestDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<Long> fileIdList = trashRequestDto.getFileIdList();
        List<String> prefixList = trashRequestDto.getPrefixList().stream()
                .map(this::makeMemberPrefix)
                .toList();
        if (!fileIdList.isEmpty()) {
            fileRepository.softDeleteFilesByIds(fileIdList, memberId);
        }
        if (!prefixList.isEmpty()) {
            fileRepository.softDeleteFilesByPrefixes(prefixList, memberId);
        }
    }

    // [/restore]
    @Override
    public void restore(RestoreRequestDto restoreRequestDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<Long> fileIdList = restoreRequestDto.getFileIdList();
        List<String> prefixList = restoreRequestDto.getPrefixList().stream()
                .map(this::makeMemberPrefix)
                .toList();
        if (!fileIdList.isEmpty()) {
            fileRepository.restoreFilesByIds(fileIdList, memberId);
        }
        if (!prefixList.isEmpty()) {
            fileRepository.restoreFilesByPrefixes(prefixList, memberId);
        }
    }

    // [/trash/delete]
    @Override
    public void delete(DeleteRequestDto deleteRequestDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<Long> fileIdList = deleteRequestDto.getFileIdList();
        List<String> prefixList = deleteRequestDto.getPrefixList().stream()
                .map(this::makeMemberPrefix)
                .toList();
        List<String> s3KeyList = new ArrayList<>();
        if (!fileIdList.isEmpty()) {
        	s3KeyList.addAll(fileRepository.findS3KeysByFileIds(fileIdList, memberId));
            fileRepository.deleteFilesByIds(fileIdList, memberId);
        }
        if (!prefixList.isEmpty()) {
        	s3KeyList.addAll(fileRepository.findS3KeysByPrefixes(prefixList, memberId));
            fileRepository.deleteFilesByPrefixes(prefixList, memberId);
        }

        // S3에서 파일을 삭제합니다.
        for(String s3Key : s3KeyList) {
        	s3Service.deleteFile(s3Key);
        	s3Service.deleteThumbnailFile(s3Key);
        }
        
        // 멤버의 usedStorage를 업데이트합니다.
        memberService.updateStorageByMemberId(memberId);
    }

    public Map<String, Object> getDirectoryStructure(boolean isDeleted) {
        String prefix = makeMemberPrefix("");
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<String> directories = fileRepository.findDirectoriesByPrefixAndMemberIdAndIsDeleted(prefix, memberId, isDeleted);

        Map<String, Object> root = new LinkedHashMap<>();

        for (String path : directories) {
            if (path.isEmpty()) continue;
            String[] parts = path.split("/");
            Map<String, Object> current = root;
            StringBuilder currentPrefix = new StringBuilder();

            for (String part : parts) {
                currentPrefix.append(part).append("/");

                if (!current.containsKey(part)) {
                    Map<String, Object> newNode = new LinkedHashMap<>();
                    newNode.put("prefix", currentPrefix.toString());
                    newNode.put("children", new LinkedHashMap<String, Object>());
                    current.put(part, newNode);
                }

                Map<String, Object> node = (Map<String, Object>) current.get(part);
                current = (Map<String, Object>) node.get("children");
            }
        }

        return root;
    }

    // prefix 앞에 member의 email을 추가합니다.
    private String makeMemberPrefix(String prefix) {
        String email = SecurityUtil.getCurrentMemberEmail();
        return String.format("%s/%s", email, prefix.replaceAll("^/+", ""));
    }


    @Override
    public SearchResultResponseDto searchByKeyWord(String keyword) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        // 해당 키워드를 갖는 디렉토리 검색
//        List<DirectoryPreviewResponseDto> directories = fileRepository.searchDirectoriesWithKeyword(memberId, keyword);

        // 해당 키워드를 갖는 파일 검색
        List<FilePreviewResponseDto> files = fileRepository.searchFilesWithKeyword(memberId, keyword);
        if (files == null) files = new ArrayList<>();
        
        for (FilePreviewResponseDto file : files) {
        	if(file.getContentType().startsWith("image")) {
        		file.setPresignedUrl(s3Service.generateThumbnailPresignedUrl(file.getS3Key()));        		
        	}
            file.setS3Key(null);
            if(file.getLatitude()!=null && file.getLongitude()!=null){
                file.setLatitude(aesUtil.decrypt(file.getLatitude()));
                file.setLongitude(aesUtil.decrypt(file.getLongitude()));
            }

            // 해시태그 추가
            file.setHashtags(hashtagService.getHashtags(file.getFileId()));
        }

        return SearchResultResponseDto.builder()
                .directories(Collections.emptyList())
                .files(files)
                .build();
    }


    @Override
    public SearchResultResponseDto searchByHashtag(String hashtag) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 해당 해시태그를 갖는 파일 검색
        List<FilePreviewResponseDto> files = fileRepository.findFilesWithHashtag(memberId, hashtag);
        if (files == null) files = new ArrayList<>();
        
        for (FilePreviewResponseDto file : files) {
        	if(file.getContentType().startsWith("image")) {
        		file.setPresignedUrl(s3Service.generateThumbnailPresignedUrl(file.getS3Key()));        		
        	}
            file.setS3Key(null);
            if(file.getLatitude()!=null && file.getLongitude()!=null){
                file.setLatitude(aesUtil.decrypt(file.getLatitude()));
                file.setLongitude(aesUtil.decrypt(file.getLongitude()));
            }

            // 해시태그 추가
            file.setHashtags(hashtagService.getHashtags(file.getFileId()));
        }
        return SearchResultResponseDto.builder()
        		.directories(Collections.emptyList())
                .files(files)
                .build();
    }

    @Override
    public SearchResultResponseDto searchByDescription(String description) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        // 해당 설명을 갖는 파일 탐색
        List<FilePreviewResponseDto> files = fileRepository.findFilesWithDescription(memberId, description);
        if (files == null) files = new ArrayList<>();
        System.out.println("files: "+files);
        
        for (FilePreviewResponseDto file : files) {
        	if(file.getContentType().startsWith("image")) {
        		file.setPresignedUrl(s3Service.generateThumbnailPresignedUrl(file.getS3Key()));        		
        	}
            file.setS3Key(null);
            if(file.getLatitude()!=null && file.getLongitude()!=null){
                file.setLatitude(aesUtil.decrypt(file.getLatitude()));
                file.setLongitude(aesUtil.decrypt(file.getLongitude()));
            }

            // 해시태그 추가
            file.setHashtags(hashtagService.getHashtags(file.getFileId()));
        }
        return SearchResultResponseDto.builder()
        		.directories(Collections.emptyList())
                .files(files)
				.build();
	}
}
