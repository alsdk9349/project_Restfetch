package com.example.backend.domain.Sse.service;

import com.example.backend.domain.Sse.repository.SseRepository;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
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

        List<Map.Entry<String, SseEmitter>> emittersList = new ArrayList<>(sseRepository.getAll().entrySet());
        Collections.reverse(emittersList);

//        sseRepository.getAll().forEach((key, emitter) -> {
        for (Map.Entry<String, SseEmitter> entry : emittersList) {
            String key = entry.getKey();
            SseEmitter emitter = entry.getValue();

            try {
                log.info("{}", key);
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .name(message) // 이벤트 이름
                        .data(reportData);
                //.reconnectTime(3000L);
                emitter.send(event); // 데이터 전송
                log.info("zzz{}", reportData);

                // 캐시된 이벤트도 함께 전송
                List<Object> cachedEvents = sseRepository.getEvents(key); // 해당 키로 캐시된 이벤트 가져오기
//                Collections.reverse(cachedEvents); // 리스트를 역순으로 변환

                for (Object cachedEvent : cachedEvents) {
                    SseEmitter.SseEventBuilder cachedEventBuilder = SseEmitter.event()
                            .name(key) // 캐시된 이벤트 이름 (필요시 변경 가능)
                            .data(cachedEvent);
                    emitter.send(cachedEventBuilder); // 캐시된 이벤트 전송
                    log.info("Cached event sent: {}", cachedEvent);
                }

            } catch (Exception e) {
                log.info("fail");
                emitter.completeWithError(e); // 에러 처리
                sseRepository.remove(key); // 구독 취소

                sseRepository.cacheEvent(key, reportData);
            }
        };
    }
}
