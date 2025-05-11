package com.ssafy.project.domain.board.service;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import com.ssafy.project.domain.board.exception.PostDeletionNotAllowedException;
import com.ssafy.project.domain.board.repository.PostRepository;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.service.helper.UploadHelper;
import com.ssafy.project.domain.member.entity.Member;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Transactional
    @Override
    public void createPost(PostRequestDto postRequestDto) {
        Member member = SecurityUtil.getCurrentMember();
        postRequestDto.setMemberId(member.getMemberId());
        postRepository.insert(postRequestDto);
    }

    @Transactional
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

    @Override
    public void deletePost(Long postId) {
        Member member = SecurityUtil.getCurrentMember();

        if(!postRepository.delete(member.getMemberId(), postId))
            throw new PostDeletionNotAllowedException("게시글 삭제에 실패했습니다.");
    }
}
