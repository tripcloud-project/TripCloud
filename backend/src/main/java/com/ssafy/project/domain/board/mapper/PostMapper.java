package com.ssafy.project.domain.board.mapper;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    int insert(PostRequestDto postRequestDto);
}
