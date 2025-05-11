package com.ssafy.project.domain.board.service;

import com.ssafy.project.domain.board.repository.PostRepository;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.service.helper.UploadHelper;
import com.ssafy.project.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final S3Service s3Service;

    @Override
    public String uploadImage(MultipartFile image) {
        try {
            String originalFilename = image.getOriginalFilename();
            String s3Key = UploadHelper.generateUuidS3Key("board/" + originalFilename);
            s3Service.uploadObject(image, s3Key);
            return s3Key;
        } catch (IOException e) {
            throw new UploadFailException("잘못된 업로드 요청입니다.");
        }
    }
}
