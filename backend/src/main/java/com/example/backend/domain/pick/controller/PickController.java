package com.example.backend.domain.pick.controller;

import com.example.backend.domain.pick.dto.request.PickRequestDto;
import com.example.backend.domain.pick.dto.response.PickCheckResponseDto;
import com.example.backend.domain.pick.dto.response.PickGetResponseDto;
import com.example.backend.domain.pick.service.PickService;
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

    @PostMapping("/request/{report_id}")
    public ResponseEntity<?> requestPick(@AuthenticationPrincipal CustomUserDetails userIn, @PathVariable("report_id") long reportId) {
        pickService.pick(reportId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_REQUEST_OK);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPick() {
        PickGetResponseDto pickGetResponseDto = pickService.getPick();
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_GET_OK, pickGetResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/check/{report_id}")
    public ResponseEntity<?> checkPick(@AuthenticationPrincipal CustomUserDetails userIn, @PathVariable("report_id") long reportId) {
        PickCheckResponseDto pickCheckResponseDto = pickService.checkPick(reportId);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.PICK_CHECK_OK, pickCheckResponseDto);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }
}

