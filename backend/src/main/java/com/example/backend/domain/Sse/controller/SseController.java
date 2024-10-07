package com.example.backend.domain.Sse.controller;

import com.example.backend.domain.Sse.service.SseService;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;

    @GetMapping(path = "/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe() {
        String sseId = UUID.randomUUID().toString(); // 클라이언트 ID 생성
        SseEmitter emitter = sseService.subscribe(sseId);
        return ResponseEntity.ok(emitter);
    }

    public void notifyNewReport(ReportGetResponseDto reportData) {
        sseService.broadcast(reportData);
    }
}
