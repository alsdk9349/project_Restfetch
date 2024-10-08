package com.example.backend.domain.Sse.service;

import com.example.backend.domain.Sse.repository.SseRepository;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {



    private final SseRepository sseRepository;

    public SseEmitter connect(String email) {
        SseEmitter emitter = new SseEmitter();

        sseRepository.save(email, emitter);

        List<Object> cachedEvents = sseRepository.getAllEvents();
        for (Object event : cachedEvents) {
            try {
                emitter.send(event); // 캐시된 이벤트 전송
            } catch (Exception e) {
                emitter.completeWithError(e); // 에러 처리

            }
        }

        emitter.onCompletion(() -> sseRepository.remove(email));
        emitter.onTimeout(() -> sseRepository.remove(email));

        // 첫 연결 시 503에러 방지 위해 더미 데이터 전송
        ReportGetResponseDto data = new ReportGetResponseDto();
        send(data, "연결 성공");


        return emitter;
    }

    public void send(ReportGetResponseDto reportData, String message) {
        log.info("Sending report to Sse");

        sseRepository.getAll().forEach((key, emitter) -> {
            try {
                log.info("key: {}, emitter: {}", key, emitter);
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .name(message) // 이벤트 이름
                        .id(String.valueOf(reportData.getReportId())) // 이벤트 ID
                        .data(message + ": " + reportData) // 메시지와 데이터를 결합하여 전송
                        .reconnectTime(3000L);
                emitter.send(event); // 데이터 전송
                log.info("{}", emitter);
            } catch (Exception e) {
                log.info("fail");
                emitter.completeWithError(e); // 에러 처리
                sseRepository.remove(key); // 구독 취소

                sseRepository.cacheEvent(key, reportData);
            }
        });
    }
}
