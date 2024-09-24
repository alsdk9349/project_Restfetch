package com.example.backend.domain.member.controller;


import com.example.backend.domain.member.dto.request.SignupRequestDto;
import com.example.backend.domain.member.service.MemberService;
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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto SignupRequestDto) {
        log.info("Signup request: {}", SignupRequestDto.getEmail());
        memberService.signup(SignupRequestDto);
        return ResponseEntity.ok().build();
    }
}
