package com.ssafy.project.domain.member.service;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import org.apache.ibatis.javassist.bytecode.DuplicateMemberException;

public interface MemberService {
    void createMember(MemberRegisterDto memberRegisterDto) throws DuplicateMemberException;
}
