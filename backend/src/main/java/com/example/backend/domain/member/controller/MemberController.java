package com.example.backend.domain.member.controller;


import com.example.backend.domain.member.dto.request.EmailCheckRequestDto;
import com.example.backend.domain.member.dto.request.EmailRequestDto;
import com.example.backend.domain.member.dto.request.SignupRequestDto;
import com.example.backend.domain.member.service.MemberService;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

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
        return ResponseEntity.status(resultResponse.getStatus()).build();
    }

    /**
     이메일 인증번호 발송
     * @param EmailRequestDto
     * @return
     */
    @PostMapping("/emails/send")
    public ResponseEntity<?> sendCode(@Valid @RequestBody EmailRequestDto EmailRequestDto) {
        log.info("Email request: {}", EmailRequestDto.getEmail());
        memberService.sendCodeToEmail(EmailRequestDto.getEmail());
        ResultResponse resultResponse = ResultResponse.of(ResultCode.EMAIL_SEND_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/emails/verify")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody EmailCheckRequestDto EmailCheckRequestDto) {
        log.info("Email verification: {}", EmailCheckRequestDto.getVerificationCode());
        memberService.verifyCode(EmailCheckRequestDto.getEmail(), EmailCheckRequestDto.getVerificationCode());
        ResultResponse resultResponse = ResultResponse.of(ResultCode.VALIDATION_NUMBER_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }


}
