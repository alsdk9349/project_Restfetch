package com.example.backend.domain.search.service;

import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.domain.search.dto.request.ReportRequestDto;
import com.example.backend.domain.search.dto.response.ReportResponseDto;
import com.example.backend.domain.search.entity.Report;
import com.example.backend.domain.search.repository.ReportRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ObserverRepository observerRepository;

    public ReportResponseDto newReport(Long observerId, ReportRequestDto requestDto) {
        log.info("New report");

        String picture = requestDto.getPicture();

        Observer observer = observerRepository.findById(observerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

        Report report = Report.builder()
                .observer(observer)
                .picture(picture)
                .isPicked(false)
                .build();

        reportRepository.save(report);

        String observerSerialNumber = observer.getObserverSerialNumber();
        Double latitude = observer.getLatitude();
        Double longitude = observer.getLongitude();

        return ReportResponseDto.builder()
                .reportId(report.getId())
                .observerId(observerId)
                .observerSerialNumber(observerSerialNumber)
                .picture(picture)
                .longitude(longitude)
                .latitude(latitude)
                .createdAt(report.getCreatedAt())
                .isPicked(report.isPicked())
                .build();
    }
}
