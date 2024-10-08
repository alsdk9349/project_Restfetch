package com.example.backend.domain.report.dto.response;

import com.example.backend.domain.report.entity.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
public class ReportGetResponseDto {

    private long reportId;
    private long observerId;
    private String observerSerialNumber;
    private String picture;
    private String pictureName;
    private LocalDateTime createdAt;
    private boolean isPicked;

    @Builder
    private ReportGetResponseDto(long reportId, long observerId, String observerSerialNumber, String picture, String pictureName, LocalDateTime createdAt, boolean isPicked) {
        this.reportId = reportId;
        this.observerId = observerId;
        this.observerSerialNumber = observerSerialNumber;
        this.picture = picture;
        this.pictureName = pictureName;
        this.createdAt = createdAt;
        this.isPicked = isPicked;
    }

    public static ReportGetResponseDto of(Long observerId, Report report) {
        return ReportGetResponseDto.builder()
                .observerId(observerId)
                .observerSerialNumber(report.getObserver().getObserverSerialNumber())
                .reportId(report.getId())
                .picture(report.getPicture())
                .pictureName(report.getPictureName())
                .createdAt(report.getCreatedAt())
                .isPicked(report.isPicked())
                .build();
    }
}
