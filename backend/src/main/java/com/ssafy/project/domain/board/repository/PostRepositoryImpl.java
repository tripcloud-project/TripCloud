package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import com.ssafy.project.domain.board.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final PostMapper postMapper;
    @Override
    public void insert(PostRequestDto postRequestDto) {
        postMapper.insert(postRequestDto);
    }
}
