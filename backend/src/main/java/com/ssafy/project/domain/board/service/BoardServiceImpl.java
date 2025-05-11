package com.ssafy.project.domain.board.service;

import com.ssafy.project.domain.board.dto.PostRequestDto;
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
    public boolean togglePostLike(Long postId) {
        Member member = SecurityUtil.getCurrentMember();

        // 이미 좋아요가 눌러져 있는지 확인
        boolean flag = postRepository.existsLikeByPostIdAndMemberId(postId, member.getMemberId());

        if (!flag) {
            // 눌러져 있지 않다면 좋아요 추가
            postRepository.insertPostLike(postId, member.getMemberId());
        } else {
            // 눌러져 있다면 좋아요 삭제
            postRepository.deletePostLike(postId, member.getMemberId());
        }

        return !flag;
    }

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
}
