package com.ssafy.project.domain.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;

@Mapper
public interface CommentMapper {
	int insert(CommentRequestDto commentRequestDto);
}
