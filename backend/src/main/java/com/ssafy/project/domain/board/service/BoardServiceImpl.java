package com.ssafy.project.domain.board.service;

import java.io.IOException;
import java.util.List;

import com.ssafy.project.domain.board.dto.response.CommentResponseDto;
import com.ssafy.project.domain.board.dto.response.PostDetailResponseDto;
import com.ssafy.project.domain.board.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.common.response.OffsetPageResponse;
import com.ssafy.project.domain.board.dto.request.CommentRequestDto;
import com.ssafy.project.domain.board.dto.request.PostRequestDto;
import com.ssafy.project.domain.board.dto.response.PostPreviewResponseDto;
import com.ssafy.project.domain.board.repository.CommentRepository;
import com.ssafy.project.domain.board.repository.PostRepository;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.service.helper.UploadHelper;
import com.ssafy.project.domain.member.entity.Member;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
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

    @Override
    public void createComment(Long postId, CommentRequestDto commentRequestDto) {
        Member member = SecurityUtil.getCurrentMember();
        commentRequestDto.setMemberId(member.getMemberId());
        commentRequestDto.setPostId(postId);

        if (!postRepository.existsByPostId(postId))
            throw new NotFoundPostException("게시글이 존재하지 않습니다.");

        if (!commentRepository.insert(commentRequestDto))
            throw new CommentInsertNotAllowedException("댓글 작성에 실패하였습니다.");
    }

    @Override
    public void deletePost(Long postId) {
        Member member = SecurityUtil.getCurrentMember();

        if (!postRepository.delete(member.getMemberId(), postId))
            throw new PostDeletionNotAllowedException("게시글 삭제에 실패했습니다.");
    }

    @Override
    public OffsetPageResponse<?> getPagedPostList(Integer page, Integer size) {
        List<PostPreviewResponseDto> postResponseList = postRepository.selectByPageAndSize(page, size);

        Boolean hasNext = postResponseList.size() == size + 1;
        List<PostPreviewResponseDto> content = hasNext ? postResponseList.subList(0, size) : postResponseList;
        Integer nextPage = page + 1;
        Integer answerSize = hasNext ? size : postResponseList.size();

        OffsetPageResponse<PostPreviewResponseDto> pageResponse = OffsetPageResponse.<PostPreviewResponseDto>builder()
                .content(content)
                .hasNext(hasNext)
                .size(answerSize)
                .nextPage(nextPage)
                .build();

        return pageResponse;
    }

    @Override
    public PostDetailResponseDto getPost(Long postId) {
        Member member = SecurityUtil.getCurrentMember();
        PostDetailResponseDto postDetailResponseDto = postRepository.selectByPostId(postId, member.getMemberId());

        // 게시글 조회 실패 시
        if (postDetailResponseDto == null)
            throw new NotFoundPostException("게시글 조회에 실패했습니다.");

        List<CommentResponseDto> comments = commentRepository.selectByPostId(postId, member.getMemberId());

        postDetailResponseDto.setLikeCount(postRepository.countLikeByPostId(postId));
        postDetailResponseDto.setComments(comments);

        return postDetailResponseDto;
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Member member = SecurityUtil.getCurrentMember();

        if (!commentRepository.delete(member.getMemberId(), postId, commentId))
            throw new CommentDeletionNotAllowedException("댓글 삭제에 실패했습니다.");

    }

    @Override
    public void updatePost(Long postId, PostRequestDto postRequestDto) {
        Member member = SecurityUtil.getCurrentMember();
        postRequestDto.setMemberId(member.getMemberId());
        postRequestDto.setPostId(postId);

        if(!postRepository.update(postRequestDto))
            throw new PostUpdateNotAllowedException("게시글 수정에 실패했습니다.");
    }
}
