package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.repository.PhotoRepository;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService{
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    @Override
    public DirectoryResponseDto select(String prefix){
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<DirectoryEntry> directories = new ArrayList<>();
        List<FileEntry> files = new ArrayList<>();


        if ("/".equals(prefix)) {
            // 시도 리스트 조회
            directories.addAll(photoRepository.findAllSidoByMemberId(memberId));
        } else if (prefix.chars().filter(c -> c == '/').count() == 1) {
            // 시군구 리스트 조회
            String province = prefix.replace("/", "");
            directories.addAll(photoRepository.findSigunguBySidoAndMemberId(province, memberId));
        } else if (prefix.chars().filter(c -> c == '/').count() == 2) {
            // 읍면동 리스트 조회
            String trimmed = prefix.endsWith("/") ? prefix.substring(0, prefix.length() - 1) : prefix;
            String[] parts = trimmed.split("/");
            String province = parts[0];
            String city = parts[1];
            files.addAll(photoRepository.findPhotosBySidoAndSigunguAndMemberId(province, city, memberId));
            for(FileEntry file : files){
                file.setPresignedUrl(s3Service.generatePresignedUrl(file.getS3Key()));
                file.setS3Key(null);
            }
        } else {
            throw new IllegalArgumentException("Invalid prefix format: " + prefix);
        }


        return DirectoryResponseDto.builder()
                .prefix(prefix)
                .directories(directories)
                .files(files)
                .totalSize(
                        directories.stream().mapToLong(d -> d.getSize() != null ? d.getSize() : 0L).sum() +
                                files.stream().mapToLong(f -> f.getSize() != null ? f.getSize() : 0L).sum()
                )
                .build();
    }
}
