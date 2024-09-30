package com.example.backend.domain.fetch.controller;

import com.example.backend.domain.fetch.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.ObserverRegisterResponseDto;
import com.example.backend.domain.fetch.entity.Observer;
import com.example.backend.domain.fetch.service.ObserverService;
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

    @PostMapping("/{fetch_id}/register")
    public ResponseEntity<?> registerObserver(@PathVariable("fetch_id") Long fetch_id, @RequestBody ObserverRegisterRequestDto requestDto) {
        ObserverRegisterResponseDto response = observerService.registerObserver(fetch_id, requestDto);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.OBSERVER_REGISTER_OK, response);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

    @PostMapping("/{fetch_id}/{observer_id}/delete")
    public ResponseEntity<?> deleteObserver(@PathVariable("fetch_id") Long fetch_id, @PathVariable("observer_id") Long observer_id) {
        observerService.deleteObserver(fetch_id, observer_id);
        ResultResponse resultResponse = ResultResponse.of(ResultCode.OBSERVER_DELETE_OK, observer_id);
        return ResponseEntity.status(resultResponse.getStatus()).body(resultResponse);
    }

}
