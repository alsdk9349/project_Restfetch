package com.example.backend.domain.fetch.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.awt.geom.Point2D;

@Data
public class ObserverRegisterRequestDto {


    @NotNull(message = "시리얼 번호를 입력해주세요.")
    private String observerSerialNumber;

    @NotNull(message = "위도를 입력해주세요.")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private Double longitude;

}
