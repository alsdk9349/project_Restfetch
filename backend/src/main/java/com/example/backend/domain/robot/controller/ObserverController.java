package com.example.backend.domain.robot.controller;

import com.example.backend.domain.robot.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.robot.dto.response.ObserverRegisterResponseDto;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.domain.robot.service.ObserverService;
import com.example.backend.domain.search.dto.request.ReportRequestDto;
import com.example.backend.domain.search.dto.response.ReportResponseDto;
import com.example.backend.domain.search.service.ReportService;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/observer")
@RequiredArgsConstructor
public class ObserverController {

    private final ObserverService observerService;
    private final ReportService reportService;

    @PostMapping("/{fetch_id}/register")
    public ResponseEntity<?> registerObserver(@PathVariable("fetch_id") Long fetch_id, @RequestBody ObserverRegisterRequestDto requestDto) {
        ObserverRegisterResponseDto response = observerService.registerObserver(fetch_id, requestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.OBSERVER_REGISTER_OK, response);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @DeleteMapping("/{fetch_id}/{observer_id}")
    public ResponseEntity<?> deleteObserver(@PathVariable("fetch_id") Long fetch_id, @PathVariable("observer_id") Long observer_id) {
        observerService.deleteObserver(fetch_id, observer_id);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.OBSERVER_DELETE_OK, observer_id);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/{observer_id}/report")
    public ResponseEntity<?> newReport(@PathVariable("observer_id") Long observer_id, @RequestBody ReportRequestDto requestDto) {
        ReportResponseDto reportResponseDto =  reportService.newReport(observer_id, requestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.REPORT_NEW_OK, reportResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }
}