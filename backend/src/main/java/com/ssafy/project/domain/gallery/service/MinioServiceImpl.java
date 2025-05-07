package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryDto;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponse;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CommonPrefix;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    
    @Value("${minio.bucket}")
    private String bucketName;

    
    // [upload]
	@Override
	public List<UploadDto> uploadFiles(List<MultipartFile> files, String prefix) {
		List<UploadDto> uploadList = new ArrayList<>();
        boolean isDirectory = hasDirectory(files);
        String resolvedPrefix = prefix;
        if (isDirectory) {
            resolvedPrefix = resolveUniquePrefix(files, prefix);
        }
        for (MultipartFile file : files) {
            try {
                String originalName = file.getOriginalFilename();
                if (originalName == null || originalName.isBlank() || originalName.endsWith("/") || file.getSize() == 0) {
                    continue;
                }
                String s3Key;
                if (isDirectory) {
                    String relativePath = originalName.substring(originalName.indexOf("/") + 1); // 상주/2025.jpg
                    s3Key = resolvedPrefix + relativePath;
                }else{
                    s3Key = generateUniqueFileKey(prefix, originalName);
                }

                uploadFile(file, s3Key);
                uploadList.add(new UploadDto(file, s3Key));

            } catch (IOException e) {
//                log.error("파일 업로드 실패: {}", file.getOriginalFilename(), e);
            }
        }
        return uploadList;
	}
	
    public void uploadFile(MultipartFile file, String s3Key) throws IOException {
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
    }
	
	public String generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(30))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
	
    public boolean existsInS3(String prefix) {
        ListObjectsV2Request req = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .maxKeys(1)
                .build();
        ListObjectsV2Response res = s3Client.listObjectsV2(req);
        return !res.contents().isEmpty();
    }
    public String generateUniqueFileKey(String prefix, String filename) {
        String name = filename;
        String extension = "";

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex != -1) {
            name = filename.substring(0, dotIndex);
            extension = filename.substring(dotIndex); // .jpg 등
        }

        String key = prefix + filename;
        int count = 1;

        while (existsInS3(key)) {
            String newName = name + " (" + count + ")" + extension;
            key = prefix + newName;
            count++;
        }

        return key;
    }
	
    private boolean hasDirectory(List<MultipartFile> files) {
        return files.stream()
                .map(MultipartFile::getOriginalFilename)
                .anyMatch(name -> name != null && name.contains("/"));
    }
    
    public String resolveUniquePrefix(List<MultipartFile> files, String prefix) {
        if (!hasDirectory(files)) {
            return prefix;
        }

        // 디렉토리 업로드일 경우: 최상위 디렉토리 이름 추출
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
    
    
    // [list]
    @Override
    public DirectoryResponse listDirectory(String prefix){
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .delimiter("/") // 슬래시를 기준으로 디렉토리 구분
                .build();

        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);

        List<DirectoryDto> entries = new ArrayList<>();

        // 하위 디렉토리 목록 추가 (크기 포함)
        for (CommonPrefix dir : listRes.commonPrefixes()) {
            String name = dir.prefix().substring(prefix.length());
            long dirSize = calculateDirectoryTotalSize(dir.prefix()); // ✅ 하위 디렉토리 용량까지 계산
            entries.add(new DirectoryDto(name, true, dirSize, null));
        }
        
        // 현재 디렉토리의 파일 목록 추가
        for (S3Object file : listRes.contents()) {
            if (!file.key().equals(prefix)) {
                String name = file.key().substring(prefix.length());
                String url = generatePresignedUrl(file.key());
                entries.add(new DirectoryDto(name, false, file.size(), url));
            }
        }

        // 디렉토리 전체 하위 포함 파일 크기 계산
        long totalSize = calculateDirectoryTotalSize(prefix);
        
        
        // 유저 이름은 보내지 않기.
        int idx = prefix.indexOf('/');
        String result = (idx != -1) ? prefix.substring(idx + 1) : "";
        return new DirectoryResponse(result, totalSize, entries);
    }
    
    private long calculateDirectoryTotalSize(String prefix) {
        return getContents(prefix).stream()
                .filter(obj -> !obj.key().equals(prefix))
                .mapToLong(S3Object::size)
                .sum();
    }
    
    public List<S3Object> getContents(String key){
        ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(key)
                .build();
        ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
        return listRes.contents();
    }
    
    
    
    // [rename]
    @Override
    public List<S3KeyUpdateDto> directoryKeyUpdate(String oldPrefix, String newPrefix) {
        List<S3KeyUpdateDto> renameList = new ArrayList<>();
        try{
            for (S3Object obj : getContents(oldPrefix)) {
                String relativePath = obj.key().substring(oldPrefix.length());
                String destinationKey = newPrefix + relativePath;

                keyUpdate(obj.key(), destinationKey);
                renameList.add(new S3KeyUpdateDto(obj.key(), destinationKey));
            }
        }catch (Exception e){

        }
        return renameList;
    }
    
    @Override
    public void keyUpdate(String oldKey, String newKey) {
        copyFile(oldKey, newKey);
        deleteFile(oldKey);
    }
    
    public void copyFile(String oldKey, String newKey){
        s3Client.copyObject(CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(oldKey)
                .destinationBucket(bucketName)
                .destinationKey(newKey)
                .build());
    }
    public void deleteFile(String key){
        s3Client.deleteObject(builder -> builder
                .bucket(bucketName)
                .key(key)
                .build());
    }
    
}
