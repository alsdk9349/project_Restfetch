package com.example.backend.domain.Sse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SseResponseDto {
    private String message;
    private long reportId;
    private String picture;
}
