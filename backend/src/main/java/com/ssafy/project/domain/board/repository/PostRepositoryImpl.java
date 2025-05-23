package com.ssafy.project.domain.board.repository;

import java.util.List;

import com.ssafy.project.domain.board.dto.response.PostDetailResponseDto;
import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.board.dto.request.PostRequestDto;
import com.ssafy.project.domain.board.dto.response.PostPreviewResponseDto;
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
    public boolean delete(@NonNull Long memberId, Long postId) {
        return postMapper.delete(memberId, postId) == 1;
    }

    @Override
    public boolean existsLikeByPostIdAndMemberId(Long postId, Long memberId) {
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

    @Override
    public List<PostPreviewResponseDto> selectByPageAndSize(Integer page, int size) {
        return postMapper.selectByOffsetAndSize(page * size, size + 1);
    }

    @Override
    public PostDetailResponseDto selectByPostId(Long postId, Long memberId) {
        return postMapper.selectByPostId(postId, memberId);
    }

    @Override
    public int countLikeByPostId(@NonNull Long postId) {
        return postMapper.countLikeByPostId(postId);
    }

    @Override
    public boolean update(PostRequestDto postRequestDto) {
        return postMapper.update(postRequestDto) == 1;
    }

	@Override
	public Integer getTotalCount() {
		return postMapper.count();
	}
}
