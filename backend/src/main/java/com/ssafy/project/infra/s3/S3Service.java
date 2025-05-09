package com.ssafy.project.infra.s3;

import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import com.ssafy.project.domain.gallery.exception.UploadFailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${minio.bucket}")
    private String bucketName;

    
    
    // [upload]
    public List<UploadDto> uploadObjects(List<MultipartFile> files, String prefix){
        if (isDirectory(files)) {
            return uploadDirectoryFiles(files, prefix);
        }else {
        	return uploadFiles(files, prefix);
        }
    }
    
    public void uploadObject(MultipartFile file, String s3Key) throws IOException {
    	s3Client.putObject(
    			PutObjectRequest.builder()
    			.bucket(bucketName)
    			.key(s3Key)
    			.contentType(file.getContentType())
    			.build(),
    			RequestBody.fromInputStream(file.getInputStream(), file.getSize())
    			);
    }
    
    // [upload - directories]
    private List<UploadDto> uploadDirectoryFiles(List<MultipartFile> files, String prefix) {
    	List<UploadDto> uploadList = new ArrayList<>();
    	String resolvedPrefix = resolveUniquePrefix(files, prefix);
    	
        for (MultipartFile file : files) {
            try {
            	if(file.isEmpty()) continue;
                String originalPath = file.getOriginalFilename();
                String originalFilename = Paths.get(originalPath).getFileName().toString();
                String relativePath = originalPath.substring(originalPath.indexOf("/") + 1);
                String s3Key = generateUuidS3Key(resolvedPrefix + relativePath);
                uploadObject(file, s3Key);
                uploadList.add(new UploadDto(file, s3Key, originalFilename));
            } catch (IOException e) {
                log.error("파일 업로드 실패: {}", file.getOriginalFilename(), e);
                throw new UploadFailException("S3에 파일 업로드 실패");
            }
        }
        return uploadList;
    }
    
    
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
    
    private boolean existsInS3(String prefix) {
        ListObjectsV2Request req = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .maxKeys(1)
                .build();
        ListObjectsV2Response res = s3Client.listObjectsV2(req);
        return !res.contents().isEmpty();
    }
    
    // [upload - files]
	private List<UploadDto> uploadFiles(List<MultipartFile> files, String prefix) {
        List<UploadDto> uploadList = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
            	if(file.isEmpty()) continue;
                String originalFilename = file.getOriginalFilename();
                String s3Key = generateUuidS3Key(prefix + originalFilename);
                uploadObject(file, s3Key);
                uploadList.add(new UploadDto(file, s3Key, originalFilename));
            } catch (IOException e) {
                log.error("파일 업로드 실패: {}", file.getOriginalFilename(), e);
                throw new UploadFailException("S3에 파일 업로드 실패");
            }
        }
        return uploadList;
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
	
	public String generateUuidS3Key(String s3Key) {
		String filename = Paths.get(s3Key).getFileName().toString(); // "photo.jpg"
		String uuid = UUID.randomUUID().toString();

		// 확장자 유지
		int dotIndex = filename.lastIndexOf('.');
		String extension = (dotIndex != -1) ? filename.substring(dotIndex) : "";
		String newFilename = uuid + extension;

		// 경로만 따로 추출
		String parentPath = s3Key.substring(0, s3Key.lastIndexOf("/") + 1);
		String newS3Key = parentPath + newFilename;
		return newS3Key;
	}

	
    private boolean isDirectory(List<MultipartFile> files) {
        return files.stream()
                .map(MultipartFile::getOriginalFilename)
                .anyMatch(name -> name != null && name.contains("/"));
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
    public void directoryKeyUpdate(String oldPrefix, String newPrefix) {
    	for (S3Object obj : getContents(oldPrefix)) {
    		String relativePath = obj.key().substring(oldPrefix.length());
    		String destinationKey = newPrefix + relativePath;
    		keyUpdate(obj.key(), destinationKey);
    	}
    }
    
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
