package com.example.backend.domain.robot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ObserverRegisterResponseDto {
    private long observerId;
    private String observerSerialNumber;
    private Double latitude;
    private Double longitude;
}
