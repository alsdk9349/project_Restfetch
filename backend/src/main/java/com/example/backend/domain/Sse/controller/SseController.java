package com.example.backend.domain.Sse.controller;

import com.example.backend.domain.Sse.service.SseService;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import com.example.backend.global.security.CustomUserDetails;
import com.example.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;

    @GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal CustomUserDetails userDetails                                              ) {

        SseEmitter emitter = sseService.connect(userDetails.getEmail());
        return ResponseEntity.ok(emitter);
    }

    public void send(ReportGetResponseDto reportData) {
        sseService.send(reportData);
    }
}
