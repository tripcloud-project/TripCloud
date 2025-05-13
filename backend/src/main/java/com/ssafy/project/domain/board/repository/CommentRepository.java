package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;
import com.ssafy.project.domain.board.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentRepository {
    boolean insert(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> selectByPostId(Long postId, Long memberId);
}
