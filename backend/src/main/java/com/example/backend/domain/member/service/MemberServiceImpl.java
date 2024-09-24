package com.example.backend.domain.member.service;

import com.example.backend.domain.member.dto.request.SignupRequestDto;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

    @Override
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) throws BusinessException {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String nickname = signupRequestDto.getNickname();
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<Member> memberNickname = memberRepository.findByNickname(nickname);
        if(memberNickname.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_DUPLICATED);
        }

        Optional<Member> memberEmail = memberRepository.findByEmail(email);
        if(memberEmail.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_DUPLICATED);
        }

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();

        memberRepository.save(member);

    }
}
