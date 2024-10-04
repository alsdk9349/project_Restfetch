package com.example.backend.domain.pick.controller;

import com.example.backend.domain.pick.dto.PickRequestDto;
import com.example.backend.domain.pick.service.PickService;
import com.example.backend.domain.robot.dto.response.FetchRegisterResponseDto;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pick")
@RequiredArgsConstructor
public class PickController {
    private final PickService pickService;

//    @PostMapping("/request")
//    public ResponseEntity<?> requestPick(@RequestBody PickRequestDto pickRequestDto, HttpServletRequest request) {
//
//    }

}

