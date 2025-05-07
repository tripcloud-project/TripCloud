package com.ssafy.project.domain.auth.service;

import com.ssafy.project.domain.member.entity.Member;
import com.ssafy.project.domain.member.repository.MemberRepository;
import com.ssafy.project.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.selectById(Long.parseLong(memberId));
        if (member == null || member.getIsDeleted())
            throw new NotFoundMemberException();
        return new MemberDetails(member);
    }
}
