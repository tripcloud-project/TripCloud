package com.ssafy.project.domain.board.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;
import com.ssafy.project.domain.board.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{
	private final CommentMapper commentMapper;
	
	@Override
	public boolean insert(CommentRequestDto commentRequestDto) {
		if(commentMapper.insert(commentRequestDto) == 0)
			return false;
		else
			return true;
	}

}
