package com.ssafy.project.domain.gallery.service.helper;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;

public class UploadHelper {
    // uuid를 붙인 유니크 키를 생성합니다.
    public static String generateUuidS3Key(String s3Key) {
        String filename = Paths.get(s3Key).getFileName().toString(); // "photo.jpg"
        String uuid = UUID.randomUUID().toString();

        // 확장자 유지
        int dotIndex = filename.lastIndexOf('.');
        String extension = (dotIndex != -1) ? filename.substring(dotIndex) : "";
        String newFilename = uuid + extension;

        // 경로만 따로 추출
        String parentPath = s3Key.substring(0, s3Key.lastIndexOf("/") + 1);
        return parentPath + newFilename;
    }

    // 파일이 업로드될 때, 여러 파일과 여러 디렉토리가 함께 올 수 있어요.
    public static Map<String, List<MultipartFile>> groupFilesByDirectory(List<MultipartFile> files) {
        Map<String, List<MultipartFile>> grouped = new HashMap<>();

        for (MultipartFile file : files) {
            String fullPath = file.getOriginalFilename(); // 예: "여행/독도/img1.jpg"
            if (fullPath == null) continue;

            // 디렉토리 이름 추출 (마지막 슬래시 앞까지)
            String dir = fullPath.contains("/") ? fullPath.substring(0, fullPath.indexOf("/")) : "";

            grouped.computeIfAbsent(dir, k -> new ArrayList<>()).add(file);
        }
        return grouped;
    }
}
