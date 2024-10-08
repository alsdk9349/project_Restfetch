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
        send(data);


        return emitter;
    }

    public void send(ReportGetResponseDto reportData) {
        log.info("Sending report to Sse");

        sseRepository.getAll().forEach((key, emitter) -> {
            try {
                log.info("key: {}, emitter: {}", key, emitter);
                emitter.send(reportData); // 데이터 전송
                log.info("{}",reportData.getReportId());
            } catch (Exception e) {
                log.info("fail");
                emitter.completeWithError(e); // 에러 처리
                sseRepository.remove(key); // 구독 취소

                sseRepository.cacheEvent(key, reportData);
            }
        });
    }
}
