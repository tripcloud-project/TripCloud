package com.ssafy.project.domain.board.mapper;

import java.util.List;

import com.ssafy.project.domain.board.dto.response.PostDetailResponseDto;
import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.board.dto.request.PostRequestDto;
import com.ssafy.project.domain.board.dto.response.PostPreviewResponseDto;

import lombok.NonNull;

@Mapper
public interface PostMapper {
    int insert(PostRequestDto postRequestDto);

    int delete(@NonNull Long memberId, Long postId);

    boolean existsLikeByPostIdAndMemberId(Long postId, @NonNull Long memberId);

    void insertPostLike(Long postId, @NonNull Long memberId);

    void deletePostLike(Long postId, @NonNull Long memberId);

    boolean existsByPostId(Long postId);

    List<PostPreviewResponseDto> selectByOffsetAndSize(int offset, int size);

    PostDetailResponseDto selectByPostId(Long postId, Long memberId);

    int countLikeByPostId(@NonNull Long postId);

    int update(PostRequestDto postRequestDto);

	Integer count();
}
