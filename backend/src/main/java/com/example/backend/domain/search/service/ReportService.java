package com.example.backend.domain.search.service;

import com.example.backend.domain.search.dto.request.ReportRequestDto;
import com.example.backend.domain.search.dto.response.ReportResponseDto;

public interface ReportService {

    ReportResponseDto newReport(ReportRequestDto requestDto);
}
