package com.example.backend.domain.report.dto.response;

import com.example.backend.domain.robot.entity.Observer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportListResponseDto {

    private int reportId;
    private long observerId;
    private String picture;
    private LocalDateTime createdAt;

}