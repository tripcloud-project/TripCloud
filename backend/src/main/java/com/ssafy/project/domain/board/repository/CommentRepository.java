package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;

public interface CommentRepository {
	boolean insert(CommentRequestDto commentRequestDto);
}
