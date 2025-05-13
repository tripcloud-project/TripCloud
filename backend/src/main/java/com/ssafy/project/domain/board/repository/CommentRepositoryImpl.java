package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.response.CommentResponseDto;
import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;
import com.ssafy.project.domain.board.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

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

	@Override
	public List<CommentResponseDto> selectByPostId(Long postId, Long memberId) {
		return commentMapper.selectByPostId(postId, memberId);
	}

}
