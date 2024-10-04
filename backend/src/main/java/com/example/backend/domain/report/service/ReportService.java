package com.example.backend.domain.report.service;

import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReporGetResponseDto;

import java.util.List;

public interface ReportService {

    ReporGetResponseDto newReport(ReportRequestDto requestDto);
    List<ReporGetResponseDto> getReports(Long observerId);
}
