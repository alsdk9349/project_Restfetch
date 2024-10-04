package com.example.backend.domain.pick.dto.response;

import com.example.backend.domain.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class PickCheckResponseDto {

    private long reportId;
    private boolean isPicked;

    private PickCheckResponseDto(long reportId, boolean isPicked) {
        this.reportId = reportId;
        this.isPicked = isPicked;
    }

    public static PickCheckResponseDto of(Report report) {
        return PickCheckResponseDto.builder()
                .reportId(report.getId())
                .isPicked(report.isPicked())
                .build();
    }
}
