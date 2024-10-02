package com.example.backend.domain.robot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FetchRegisterRequestDto {

    @NotNull(message = "시리얼 번호를 입력해주세요.")
    private String fetchSerialNumber;

    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;


}
