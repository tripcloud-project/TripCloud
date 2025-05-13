package com.ssafy.project.domain.board.mapper;

import com.ssafy.project.domain.board.dto.response.CommentResponseDto;
import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;

import java.util.List;

@Mapper
public interface CommentMapper {
    int insert(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> selectByPostId(Long postId, Long memberId);
}
