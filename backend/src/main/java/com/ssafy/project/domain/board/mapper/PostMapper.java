package com.ssafy.project.domain.board.mapper;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import lombok.NonNull;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    int insert(PostRequestDto postRequestDto);
    int delete(@NonNull Long memberId, Long postId);
    boolean existsLikeByPostIdAndMemberId(Long postId, @NonNull Long memberId);

    void insertPostLike(Long postId, @NonNull Long memberId);

    void deletePostLike(Long postId, @NonNull Long memberId);
}
