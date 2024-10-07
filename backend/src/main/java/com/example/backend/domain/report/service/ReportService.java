package com.example.backend.domain.report.service;

import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;

import java.util.List;

public interface ReportService {

    ReportGetResponseDto newReport(ReportRequestDto requestDto);
    List<ReportGetResponseDto> getReports(Long observerId);
}
