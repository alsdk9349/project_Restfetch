package com.example.backend.domain.pick.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PickGetResponseDto {

    private long pickId;
    private long reportId;
    private long observerId;
    private String observerSerialNumber;
    private double latitude;
    private double longitude;
}
