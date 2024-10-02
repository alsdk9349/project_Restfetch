package com.example.backend.domain.fetch.controller;

import com.example.backend.domain.fetch.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.FetchRegisterResponseDto;
import com.example.backend.domain.fetch.service.FetchService;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class FetchController {

    private final FetchService fetchService;

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

//    @GetMapping("")
//    public ResponseEntity<?> fetch(HttpServletRequest request) {}

}
