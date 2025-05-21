package com.ssafy.project.domain.gallery.service.helper;

import org.springframework.http.ContentDisposition;

import java.nio.charset.StandardCharsets;

public class DownloadHelper {
    // 디렉토리 이름에 .zip을 추가합니다.
    public static String getZipFilenameFromKey(String prefix) {
        String dirName = prefix;
        if (dirName.endsWith("/")) {
            dirName = dirName.substring(0, dirName.length() - 1);
        }
        dirName = dirName.substring(dirName.lastIndexOf('/') + 1);
        return dirName + ".zip";
    }

    // Content-Disposition 헤더를 UTF-8으로 설정하여 파일명에 한글, 공백이 있을 경우에도 올바르게 처리
    public static String makeContentDisposition(String filename){
        return ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build()
                .toString();
    }
}
