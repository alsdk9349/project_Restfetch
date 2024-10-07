package com.example.backend.domain.report.controller;

import com.example.backend.domain.report.dto.response.ReportListResponseDto;
import com.example.backend.domain.report.service.ReportService;
import com.example.backend.domain.report.service.ReportServiceImpl;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reportList")
    public ResponseEntity<?> reportList(HttpServletRequest request) {
        List<ReportListResponseDto> reportListResponseDtos = reportService.getReportList(request);
        ResultResponse response = ResultResponse.of(ResultCode.REPORT_LIST_OK, reportListResponseDtos);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
