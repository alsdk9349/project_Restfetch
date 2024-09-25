package com.example.backend.domain.member.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserInfoDto {
    private long memberId;
    private String nickname;
    private String email;
    private String password;
}
