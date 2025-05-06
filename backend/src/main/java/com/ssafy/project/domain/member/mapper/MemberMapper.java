package com.ssafy.project.domain.member.mapper;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int insert(MemberRegisterDto memberRegisterDto);
    boolean existsByEmail(String email);
}
