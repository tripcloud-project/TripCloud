package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioService {
    public List<UploadDto> uploadFiles(List<MultipartFile> files, String prefix) {
        return null;
    }
}
