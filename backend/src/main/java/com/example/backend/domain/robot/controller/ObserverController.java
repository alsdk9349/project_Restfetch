package com.example.backend.domain.robot.controller;

import com.example.backend.domain.robot.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.robot.dto.response.ObserverRegisterResponseDto;
import com.example.backend.domain.robot.service.ObserverService;
import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReporGetResponseDto;
import com.example.backend.domain.report.service.ReportService;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import com.example.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/observer")
@RequiredArgsConstructor
public class ObserverController {

    private final ObserverService observerService;
    private final ReportService reportService;

    @PostMapping("/register")
    public ResponseEntity<?> registerObserver(@AuthenticationPrincipal CustomUserDetails userIn,  @RequestBody ObserverRegisterRequestDto requestDto) {
        ObserverRegisterResponseDto response = observerService.registerObserver(requestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.OBSERVER_REGISTER_OK, response);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @DeleteMapping("/{fetch_id}/{observer_id}")
    public ResponseEntity<?> deleteObserver(@AuthenticationPrincipal CustomUserDetails userIn, @PathVariable("fetch_id") Long fetch_id, @PathVariable("observer_id") Long observer_id) {
        observerService.deleteObserver(fetch_id, observer_id);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.OBSERVER_DELETE_OK, observer_id);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/report")
    public ResponseEntity<?> newReport(@RequestBody ReportRequestDto requestDto) {
        ReporGetResponseDto reportResponseDto =  reportService.newReport(requestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.REPORT_NEW_OK, reportResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @GetMapping("/{observer_id}/reports")
    public ResponseEntity<?> getReports(@AuthenticationPrincipal CustomUserDetails userIn, @PathVariable("observer_id") Long observer_id) {
        List<ReporGetResponseDto> reportResponseDto = reportService.getReports(observer_id);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.REPORT_GET_OK, reportResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }
}