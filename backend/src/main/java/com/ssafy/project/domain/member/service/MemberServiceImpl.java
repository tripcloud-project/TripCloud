package com.ssafy.project.domain.member.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.project.common.response.PageResponse;
import com.ssafy.project.domain.auth.service.MemberDetails;
import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.response.ActivityResponseDto;
import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;
import com.ssafy.project.domain.member.dto.response.MemberResponseDto;
import com.ssafy.project.domain.member.dto.response.ValidateEmailResponseDto;
import com.ssafy.project.domain.member.entity.Member;
import com.ssafy.project.domain.member.exception.DuplicateMemberException;
import com.ssafy.project.domain.member.exception.InvalidPasswordException;
import com.ssafy.project.domain.member.repository.ActivityRepository;
import com.ssafy.project.domain.member.repository.BadgeRepository;
import com.ssafy.project.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    @Override
    public void createMember(MemberRegisterDto memberRegisterDto) {

        // 이메일이 중복될 경우
        if (memberRepository.existsByEmail(memberRegisterDto.getEmail())) {
            throw new DuplicateMemberException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 조건 미충족시
        // TODO 현재는 검사 안하지만 비밀번호 조건 체크 필요
        if(isValid(memberRegisterDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호는 8~24자의 영문, 숫자, 특수문자를 포함해야 합니다.");
        }

        memberRegisterDto.setPassword(passwordEncoder.encode(memberRegisterDto.getPassword()));

        memberRepository.insertMember(memberRegisterDto);
    }

    @Override
    public ValidateEmailResponseDto isEmailDuplicated(String email) {
        return new ValidateEmailResponseDto(memberRepository.existsByEmail(email));
    }

    @Override
    public MemberResponseDto getCurrentMemberInfo(Authentication authentication) {
        Member member = ((MemberDetails) authentication.getPrincipal()).member();
        BadgeResponseDto mainBadge = badgeRepository.selectById(member.getMainBadgeId());

        return MemberResponseDto.builder()
                .email(member.getEmail())
                .role(member.getRole())
                .name(member.getName())
                .profileImage(member.getProfileImage())
                .usedStorage(member.getUsedStorage())
                .maxStorage(member.getMaxStorage())
                .mainBadge(mainBadge.getName()).build();
    }

    private boolean isValid(String rawPassword) {

        if(rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        String regex = "^[a-zA-Z\\d`~!@#$%^&*()\\-_=+]{8,24}$";

        RegexValidator validator = new RegexValidator(regex);

        return false;
//        return validator.isValid(rawPassword);
    }

	@Override
	public PageResponse<?> getMyActivities(Authentication authentication, LocalDateTime cursor,
			Integer size) {
        Member member = ((MemberDetails) authentication.getPrincipal()).member();
        
        List<ActivityResponseDto> activityList = activityRepository.selectByMemberIdAfterCursor(member.getMemberId(), cursor, size+1);
        
        Boolean hasNext = activityList.size() == size + 1;
        List<ActivityResponseDto> content = hasNext ? activityList.subList(0, size) : activityList;
        LocalDateTime nextCursor = activityList.get(size-1).getCreatedAt();
        Integer answerSize = hasNext ? size : activityList.size();
        
        PageResponse<ActivityResponseDto> pageResponse = PageResponse.<ActivityResponseDto>builder()
        		.content(content)
        		.hasNext(hasNext)
        		.size(answerSize)
        		.nextCursor(nextCursor)
        		.build();
        
        return pageResponse;
	}
}
