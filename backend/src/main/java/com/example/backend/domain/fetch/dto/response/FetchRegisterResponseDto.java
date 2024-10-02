package com.example.backend.domain.fetch.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FetchRegisterResponseDto {
    private long fetchId;
    private String nickname;
    private String fetchSerialNumber;
}
