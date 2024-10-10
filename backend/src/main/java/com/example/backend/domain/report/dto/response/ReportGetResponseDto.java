package com.example.backend.domain.report.dto.response;

import com.example.backend.domain.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportGetResponseDto {

    private long reportId;
    private long observerId;
    private String observerSerialNumber;
    private String picture;
    private String pictureName;
    private String message;
    private LocalDateTime createdAt;
    private boolean isPicked;

    @Builder
    private ReportGetResponseDto(long reportId, long observerId, String observerSerialNumber, String picture, String pictureName, boolean isPicked, String message) {
        this.reportId = reportId;
        this.observerId = observerId;
        this.observerSerialNumber = observerSerialNumber;
        this.picture = picture;
        this.pictureName = pictureName;
        this.createdAt = LocalDateTime.now();;
        this.isPicked = isPicked;
        this.message = message;
    }

    public static ReportGetResponseDto of(Long observerId, Report report) {
        return ReportGetResponseDto.builder()
                .observerId(observerId)
                .observerSerialNumber(report.getObserver().getObserverSerialNumber())
                .reportId(report.getId())
                .picture(report.getPicture())
                .pictureName(report.getPictureName())
                .isPicked(report.isPicked())
                .build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"message\": ").append(message).append(", ");
        sb.append("\"reportId\": ").append(reportId).append(", ");
        sb.append("\"observerId\": ").append(observerId).append(", ");
        sb.append("\"observerSerialNumber\": \"").append(observerSerialNumber).append("\", ");
        sb.append("\"picture\": \"").append(picture).append("\", ");
        sb.append("\"pictureName\": \"").append(pictureName).append("\", ");
        sb.append("\"createdAt\": \"").append(createdAt).append("\", ");
        sb.append("\"isPicked\": ").append(isPicked).append(" }");

        return sb.toString();
    }
}
