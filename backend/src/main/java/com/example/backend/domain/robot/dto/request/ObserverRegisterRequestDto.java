package com.example.backend.domain.robot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ObserverRegisterRequestDto {


    @NotNull(message = "옵저버 시리얼 번호를 입력해주세요.")
    private String observerSerialNumber;

    @NotNull(message = "패치 시리얼 번호를 입력해주세요.")
    private String fetchSerialNumber;

    @NotNull(message = "위도를 입력해주세요.")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private Double longitude;

    @NotNull(message = "옵저버 위치를 입력해주세요.")
    private String location;

}
