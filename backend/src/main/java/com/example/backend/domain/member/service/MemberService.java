package com.example.backend.domain.member.service;

import com.example.backend.domain.member.dto.request.LoginRequestDto;
import com.example.backend.domain.member.dto.request.SignupRequestDto;
import com.example.backend.domain.member.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response);
    void signup(SignupRequestDto signupRequestDto);
    void sendCodeToEmail(String Email);
    void verifyCode(String code, String userCode);
}
