package com.example.backend.domain.robot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObserverGetResponseDto {
    private long observerId;
    private String observerSerialNumber;
    private Double latitude;
    private Double longitude;
    private String location;
}
