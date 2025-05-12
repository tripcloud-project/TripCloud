package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import lombok.NonNull;

public interface PostRepository {
    void insert(PostRequestDto postRequestDto);
    boolean existsLikeByPostIdAndMemberId(Long postId, @NonNull Long memberId);
    void insertPostLike(Long postId, @NonNull Long memberId);
    void deletePostLike(Long postId, @NonNull Long memberId);
}
