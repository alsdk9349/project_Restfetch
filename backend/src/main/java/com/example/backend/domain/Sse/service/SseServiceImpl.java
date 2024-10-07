package com.example.backend.domain.Sse.service;

import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseServiceImpl implements SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String id) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(id, emitter);

        emitter.onCompletion(() -> emitters.remove(id));
        emitter.onTimeout(() -> emitters.remove(id));

        return emitter;
    }

    public void broadcast(ReportGetResponseDto reportData) {
        emitters.forEach((sseId, emitter) -> {
            try {
                emitter.send(reportData); // 데이터 전송
            } catch (Exception e) {
                emitter.completeWithError(e); // 에러 처리
                emitters.remove(sseId); // 구독 취소
            }
        });
    }
}
