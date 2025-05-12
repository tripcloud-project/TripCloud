package com.ssafy.project.domain.board.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.board.dto.request.PostRequestDto;
import com.ssafy.project.domain.board.mapper.PostMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final PostMapper postMapper;

    @Override
    public void insert(PostRequestDto postRequestDto) {
        postMapper.insert(postRequestDto);
    }

    @Override
    public boolean existsLikeByPostIdAndMemberId(Long postId, @NonNull Long memberId) {
        return postMapper.existsLikeByPostIdAndMemberId(postId, memberId);
    }

    @Override
    public void insertPostLike(Long postId, @NonNull Long memberId) {
        postMapper.insertPostLike(postId, memberId);
    }

    @Override
    public void deletePostLike(Long postId, @NonNull Long memberId) {
        postMapper.deletePostLike(postId, memberId);
    }

	@Override
	public boolean existsByPostId(Long postId) {
		return postMapper.existsByPostId(postId);
	}
}
