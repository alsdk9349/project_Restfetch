package com.example.backend.domain.pick.controller;

import com.example.backend.domain.Sse.service.SseService;
import com.example.backend.domain.pick.dto.request.PickGetRequestDto;
import com.example.backend.domain.pick.dto.response.PickCheckResponseDto;
import com.example.backend.domain.pick.dto.response.PickGetResponseDto;
import com.example.backend.domain.pick.service.PickService;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import com.example.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pick")
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;
    private final SseService sseService;

    @PostMapping("/request/{report_id}")
    public ResponseEntity<?> requestPick(@AuthenticationPrincipal CustomUserDetails userIn, @PathVariable("report_id") long reportId) {
        pickService.requestPick(reportId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_REQUEST_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/get")
    public ResponseEntity<?> getPick(@RequestBody PickGetRequestDto pickGetRequestDto) {
        PickGetResponseDto pickGetResponseDto = pickService.getPick(pickGetRequestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_GET_OK, pickGetResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/check/{report_id}")
    public ResponseEntity<?> checkPick(@AuthenticationPrincipal CustomUserDetails userIn, @PathVariable("report_id") long reportId) {
        PickCheckResponseDto pickCheckResponseDto = pickService.checkPick(reportId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_CHECK_OK, pickCheckResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/{report_id}")
    public ResponseEntity<?> pick(@PathVariable("report_id") long reportId) {
        ReportGetResponseDto responseDto = pickService.pick(reportId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_CHECK_OK, responseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

}

