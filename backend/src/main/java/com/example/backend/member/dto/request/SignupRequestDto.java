package com.example.backend.member.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @NotNull(message = "아이디는 필수 입력값입니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Email
    private String email;
}
