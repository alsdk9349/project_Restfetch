package com.example.backend.global.result;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // 기본형
    COMMON_OK(HttpStatus.OK, "C001", "요청을 처리했습니다."),

    // Member
    LOGIN_OK(HttpStatus.OK, "M001", "로그인 했습니다."),
    SIGNUP_OK(HttpStatus.CREATED, "M002", "회원가입에 성공했습니다."),
    EMAIL_SEND_OK(HttpStatus.ACCEPTED, "M003", "이메일 전송에 성공했습니다."),
    VALIDATION_NUMBER_OK(HttpStatus.OK, "M004", "이메일 인증이 성공했습니다."),
    LOGOUT_OK(HttpStatus.OK , "M005", "로그아웃 했습니다."),

    // Fetch
    FETCH_REGISTER_OK(HttpStatus.OK, "F001", "패치 등록 성공했습니다."),
    FETCH_DELETE_OK(HttpStatus.OK, "F002", "패치 삭제 성공했습니다."),
    SEARCH_FETCH_OK(HttpStatus.OK, "F003", "패치 조회 성공했습니다."),

    // Observer
    OBSERVER_REGISTER_OK(HttpStatus.OK, "O001", "옵저버 등록 성공했습니다."),
    OBSERVER_DELETE_OK(HttpStatus.OK, "O002", "옵저버 삭제 성공했습니다."),
    SEARCH_OBSERVER_OK(HttpStatus.OK, "O003", "옵저버 조회 성공했습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
