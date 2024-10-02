package com.example.backend.domain.robot.controller;

import com.example.backend.domain.robot.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.robot.dto.response.FetchGetResponseDto;
import com.example.backend.domain.robot.dto.response.FetchRegisterResponseDto;
import com.example.backend.domain.robot.dto.response.ObserverGetResponseDto;
import com.example.backend.domain.robot.service.FetchService;
import com.example.backend.domain.robot.service.ObserverService;
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
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class FetchController {

    private final FetchService fetchService;
    private final ObserverService observerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerFetch(@RequestBody FetchRegisterRequestDto requestDto, HttpServletRequest request) {
        FetchRegisterResponseDto response = fetchService.registerFetch(requestDto, request);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.FETCH_REGISTER_OK, response);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);

    }

    @DeleteMapping("/{fetch_id}")
    public ResponseEntity<?> deleteFetch(@PathVariable Long fetch_id, HttpServletRequest request) {
        fetchService.deleteFetch(fetch_id, request);
        ResultResponse response =  ResultResponse.of(ResultCode.FETCH_DELETE_OK, fetch_id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<?> fetch(HttpServletRequest request) {
        List<FetchGetResponseDto> fetchGetResponseDtos = fetchService.getFetch(request);
        ResultResponse response = ResultResponse.of(ResultCode.FETCH_SEARCH_OK, fetchGetResponseDtos);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{fetch_id}/observerList")
    public ResponseEntity<?> observerList(@PathVariable Long fetch_id) {
        List<ObserverGetResponseDto> observerGetResponseDtos = observerService.getObserver(fetch_id);
        ResultResponse response = ResultResponse.of(ResultCode.OBSERVER_SEARCH_OK, observerGetResponseDtos);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
