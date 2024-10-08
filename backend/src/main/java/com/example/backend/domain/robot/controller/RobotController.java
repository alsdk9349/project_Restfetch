package com.example.backend.domain.robot.controller;

import com.example.backend.domain.robot.dto.response.AllListResponseDto;
import com.example.backend.domain.robot.service.RobotService;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.result.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;

    @GetMapping("/allList")
    public ResponseEntity<?> allList(HttpServletRequest request) {
        AllListResponseDto allListResponseDtos = robotService.getAllList(request);
        ResultResponse response = ResultResponse.of(ResultCode.ALL_LIST_GET_OK, allListResponseDtos);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}

