package com.ssafy.project.domain.board.repository;

import java.util.List;

import com.ssafy.project.domain.board.dto.request.PostRequestDto;
import com.ssafy.project.domain.board.dto.response.PostDetailResponseDto;
import com.ssafy.project.domain.board.dto.response.PostPreviewResponseDto;

import lombok.NonNull;

public interface PostRepository {
    void insert(PostRequestDto postRequestDto);

    boolean delete(@NonNull Long memberId, Long postId);

    boolean existsLikeByPostIdAndMemberId(Long postId, Long memberId);

    void insertPostLike(Long postId, @NonNull Long memberId);

    void deletePostLike(Long postId, @NonNull Long memberId);

    boolean existsByPostId(Long postId);

    List<PostPreviewResponseDto> selectByPageAndSize(Integer page, int i);

    PostDetailResponseDto selectByPostId(Long postId, Long memberId);

    int countLikeByPostId(@NonNull Long postId);

    boolean update(PostRequestDto postRequestDto);

	Integer getTotalCount();
}
