package com.example.backend.domain.member.service;

import com.example.backend.domain.member.dto.request.SignupRequestDto;

public interface MemberService {

    void signup(SignupRequestDto signupRequestDto);
//    void sendCodeToEmail(String toEmail);
//    void verifyCode(String code, String userCode);
}
