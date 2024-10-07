package com.example.backend.domain.report.service;

import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReporGetResponseDto;
import com.example.backend.domain.report.dto.response.ReportListResponseDto;
import com.example.backend.domain.robot.dto.response.FetchGetResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ReportService {

    ReporGetResponseDto newReport(ReportRequestDto requestDto);
    List<ReporGetResponseDto> getReports(Long observerId);
    List<ReportListResponseDto> getReportList(HttpServletRequest request);

}
