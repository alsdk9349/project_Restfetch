package com.example.backend.domain.fetch.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.awt.geom.Point2D;

@Builder
@Getter
@AllArgsConstructor
public class ObserverRegisterResponseDto {
    private long observerId;
    private String observerSerialNumber;
    private Point2D.Double location;
}
