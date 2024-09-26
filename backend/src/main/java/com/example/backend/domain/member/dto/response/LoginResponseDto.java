package com.example.backend.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private long memberId;
    private String nickname;
    private String email;

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "memberId=" + memberId +
                ", name='" + nickname + '\'' +
                ", email='" + email +
                '}';
    }
}
