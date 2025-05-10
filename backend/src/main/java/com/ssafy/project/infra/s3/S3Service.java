package com.ssafy.project.infra.s3;

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
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${minio.bucket}")
    private String bucketName;

    
    // [upload]
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
