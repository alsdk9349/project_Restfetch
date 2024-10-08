package com.example.backend.domain.robot.service;

import com.example.backend.domain.robot.dto.response.AllListResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface RobotService {
    AllListResponseDto getAllList(HttpServletRequest request);
}
