package com.example.backend.domain.report.service;

import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReporGetResponseDto;
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

    public ReporGetResponseDto newReport(ReportRequestDto requestDto) {
        log.info("New report");

        String picture = requestDto.getPicture();

        Observer observer = observerRepository.findByObserverSerialNumber(requestDto.getObserverSerialNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

        Report report = Report.builder()
                .observer(observer)
                .picture(picture)
                .isPicked(false)
                .build();

        reportRepository.save(report);

        String observerSerialNumber = observer.getObserverSerialNumber();
        Long observerId = observer.getObserver_id();

        return ReporGetResponseDto.builder()
                .reportId(report.getId())
                .observerId(observerId)
                .observerSerialNumber(observerSerialNumber)
                .picture(picture)
                .createdAt(report.getCreatedAt())
                .isPicked(report.isPicked())
                .build();
    }

    public List<ReporGetResponseDto> getReports(Long observerId) {
        log.info("Get reports");

        Observer observer = observerRepository.findById(observerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

        List<Report> reports = reportRepository.findByObserver(observer);

        return reports.stream()
                .map(report -> ReporGetResponseDto.of(observerId, report))
                .toList();
    }
}
