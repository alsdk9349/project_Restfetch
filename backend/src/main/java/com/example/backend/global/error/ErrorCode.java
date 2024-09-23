package com.example.backend.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 기본형
    COMMON_ETC(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "서버에 오류가 발생했습니다."),
    COMMON_NOT(HttpStatus.NOT_FOUND, "C002", "요청한 리소스를 찾을 수 없습니다."),

    //Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
    MEMBER_DUPLICATED(HttpStatus.CONFLICT, "M002", "이미 존재하는 회원입니다."),
    MEMBER_DIFF_PASSWORD(HttpStatus.BAD_REQUEST, "M003", "비밀번호가 일치하지 않습니다."),
    MEMBER_INVALID_CODE(HttpStatus.BAD_REQUEST, "M004", "유효하지 않은 인증번호입니다."),
    MEMBER_INVALID_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "M005", "Runtime : 이메일 전송에 실패하였습니다."),
    MEMBER_COOKIE_NOT_FOUND(HttpStatus.NOT_FOUND, "M006", "쿠키를 찾지 못했습니다"),
    MEMBER_NOT_ADMIN(HttpStatus.FORBIDDEN, "M007", "관리자가 아닌 사용자 입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
