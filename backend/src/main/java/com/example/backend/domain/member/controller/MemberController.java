package com.example.backend.domain.member.controller;


import com.example.backend.domain.member.dto.request.EmailCheckRequestDto;
import com.example.backend.domain.member.dto.request.EmailRequestDto;
import com.example.backend.domain.member.dto.request.LoginRequestDto;
import com.example.backend.domain.member.dto.request.SignupRequestDto;
import com.example.backend.domain.member.dto.response.LoginResponseDto;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import com.example.backend.global.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;


    /**
     이메일 인증번호 발송
     * @param EmailRequestDto
     * @return
     */
    @PostMapping("/emails/send")
    public ResponseEntity<?> sendCode(@Valid @RequestBody EmailRequestDto EmailRequestDto) {
        log.info("Email Send: {}", EmailRequestDto.getEmail());
        memberService.sendCodeToEmail(EmailRequestDto.getEmail());
        ResultResponse resultResponse = ResultResponse.of(ResultCode.EMAIL_SEND_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    /**
     * 이메일 인증번호 확인
     * @param EmailCheckRequestDto
     * @return
     */
    @PostMapping("/emails/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody EmailCheckRequestDto EmailCheckRequestDto) {
        log.info("Email verification: {}", EmailCheckRequestDto.getVerificationCode());
        memberService.verifyCode(EmailCheckRequestDto.getEmail(), EmailCheckRequestDto.getVerificationCode());
        ResultResponse resultResponse = ResultResponse.of(ResultCode.VALIDATION_NUMBER_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    /**
     * 회원가입
     * @param SignupRequestDto
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto SignupRequestDto) {
        log.info("Signup request: {}", SignupRequestDto.getEmail());
        memberService.signup(SignupRequestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.SIGNUP_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    /**
     * 로그인
     * @param loginRequestDto
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto, response);
        log.info("Login response: {}", loginResponseDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.LOGIN_OK, loginResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    /**
     * 로그아웃
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userIn, HttpServletRequest request) {
        memberService.logout(request);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.LOGOUT_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

}
