package com.example.backend.domain.report.service;

import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReporGetResponseDto;
import com.example.backend.domain.report.entity.Report;
import com.example.backend.domain.report.repository.ReportRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import io.netty.handler.codec.base64.Base64Decoder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ObserverRepository observerRepository;

    public ReporGetResponseDto newReport(ReportRequestDto requestDto) {
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
