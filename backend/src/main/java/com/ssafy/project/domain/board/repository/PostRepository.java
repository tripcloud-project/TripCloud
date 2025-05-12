package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.request.PostRequestDto;

import lombok.NonNull;

public interface PostRepository {
    void insert(PostRequestDto postRequestDto);
    boolean delete(@NonNull Long memberId, Long postId);
    boolean existsLikeByPostIdAndMemberId(Long postId, @NonNull Long memberId);
    void insertPostLike(Long postId, @NonNull Long memberId);
    void deletePostLike(Long postId, @NonNull Long memberId);
	boolean existsByPostId(Long postId);
}
