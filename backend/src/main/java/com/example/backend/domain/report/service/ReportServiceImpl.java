package com.example.backend.domain.report.service;

import com.example.backend.domain.Sse.controller.SseController;
import com.example.backend.domain.Sse.service.SseService;
import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import com.example.backend.domain.report.entity.Report;
import com.example.backend.domain.report.repository.ReportRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ObserverRepository observerRepository;
    private final SseService sseService;
    private final SseController sseController;

    public ReportGetResponseDto newReport(ReportRequestDto requestDto) {
        log.info("New report");

        Observer observer = observerRepository.findByObserverSerialNumber(requestDto.getObserverSerialNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

//        byte[] decodedBytes = Base64.getDecoder().decode(requestDto.getPicture());
//
//        String picture = new String(Base64.getDecoder().decode(Base64.getDecoder().decode(decodedBytes)), StandardCharsets.UTF_8);

        String picture = requestDto.getPicture();

        Report report = Report.builder()
                .observer(observer)
                .picture(picture)
                .isPicked(false)
                .build();

        reportRepository.save(report);

        String observerSerialNumber = observer.getObserverSerialNumber();
        Long observerId = observer.getObserver_id();

        ReportGetResponseDto responseDto = ReportGetResponseDto.builder()
                .reportId(report.getId())
                .observerId(observerId)
                .observerSerialNumber(observerSerialNumber)
                .picture(picture)
                .createdAt(report.getCreatedAt())
                .isPicked(report.isPicked())
                .build();

        sseController.notifyNewReport(responseDto);

        return responseDto;
    }

    public List<ReportGetResponseDto> getReports(Long observerId) {
        log.info("Get reports");

        Observer observer = observerRepository.findById(observerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

        List<Report> reports = reportRepository.findByObserver(observer);

        return reports.stream()
                .map(report -> ReportGetResponseDto.of(observerId, report))
                .toList();
    }
}