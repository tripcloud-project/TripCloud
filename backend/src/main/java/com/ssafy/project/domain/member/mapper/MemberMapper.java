package com.ssafy.project.domain.member.mapper;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int insert(MemberRegisterDto memberRegisterDto);
    boolean existsByEmail(String email);
    Member selectById(Long id);
}
