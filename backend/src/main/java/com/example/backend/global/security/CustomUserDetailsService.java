package com.example.backend.global.security;

import com.example.backend.domain.member.dto.request.CustomUserInfoDto;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // JWT에서 추출한 유저 식별자(memberId)와 일치하는 member가 데이터베이스에 존재하는지의 여부를 판단
    // Auth 객체(UserPasswordAuthenticationToken)를 만들 때 필요한 UserDetails 객체로 반환
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

//        CustomUserInfoDto dto = mapper.map(member, CustomUserInfoDto.class);

        CustomUserInfoDto dto = CustomUserInfoDto.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword()) // 암호화된 비밀번호는 보안상 취급 주의
                .build();

        return new CustomUserDetails(dto);
    }


}
