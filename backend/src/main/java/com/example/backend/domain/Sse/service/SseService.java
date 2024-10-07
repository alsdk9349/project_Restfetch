package com.example.backend.domain.Sse.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;

public interface SseService {

    SseEmitter connect(String SseId);
    void send(ReportGetResponseDto reportData);

}
