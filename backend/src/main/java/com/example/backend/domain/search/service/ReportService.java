package com.example.backend.domain.search.service;

import com.example.backend.domain.search.dto.request.ReportRequestDto;
import com.example.backend.domain.search.dto.response.ReportResponseDto;

import java.util.List;

public interface ReportService {

    ReportResponseDto newReport(ReportRequestDto requestDto);
    List<ReportResponseDto> getReports(Long observerId);
}
