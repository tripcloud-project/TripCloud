package com.ssafy.project.infra.s3;

import com.ssafy.project.domain.gallery.dto.internal.S3KeyOriginalFilenameDto;
import com.ssafy.project.domain.gallery.exception.DownloadFailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${minio.bucket}")
    private String bucketName;

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

    // [download]
    public Resource createZipResource(String prefix, List<S3KeyOriginalFilenameDto> s3KeyOriginalFilenames){
        try{
            // ZIP 데이터를 저장할 ByteArrayOutputStream 및 ZipOutputStream 생성
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                // 하위 객체 모두 가져오기 (디렉토리 내 모든 파일)
                for(S3KeyOriginalFilenameDto s3KeyOriginalFilename : s3KeyOriginalFilenames){
                    String s3Key = s3KeyOriginalFilename.getS3Key();

                    // 디렉토리 경로만 추출
                    int lastSlashIndex = s3Key.lastIndexOf('/');
                    String directoryPath = (lastSlashIndex != -1) ? s3Key.substring(0, lastSlashIndex + 1) : "";

                    // zip 내부 파일명: 기존 경로 + 원래 이름
                    String filenameInZip = (directoryPath + s3KeyOriginalFilename.getOriginalFilename()).substring(prefix.length());

                    try (ResponseInputStream<GetObjectResponse> s3Stream = s3Client.getObject(
                            GetObjectRequest.builder()
                                    .bucket(bucketName)
                                    .key(s3Key)
                                    .build())) {
                        zos.putNextEntry(new ZipEntry(filenameInZip));
                        s3Stream.transferTo(zos);
                        zos.closeEntry();
                    }catch(ZipException e){
                        /*
                         클라우드 내 중복 이름을 허용
                         다운로드할 때는 객체 스토리지에서 파일 스토리지로 전송하는 것이기 때문에
                         중복 이름이 불가능함. 어떻게 처리할까?
                         */
                        log.error("중복 이름 파일 스킵: {}", e.getMessage());
                    }
                }
            }  // zos.close() 자동 호출됨

            ByteArrayResource zipResource = new ByteArrayResource(baos.toByteArray());
            return zipResource;
        }catch (Exception e){
            throw new DownloadFailException("파일 다운로드 실패");
        }
    }

    // 일반 파일일 경우 원본 파일 그대로 다운로드
    public Resource createResource(String key){
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );
        return new InputStreamResource(s3Object);
    }
}
