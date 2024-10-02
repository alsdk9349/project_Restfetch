package com.example.backend.domain.robot.service;

import com.example.backend.domain.robot.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.robot.dto.response.FetchGetResponseDto;
import com.example.backend.domain.robot.dto.response.FetchRegisterResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface FetchService {

    FetchRegisterResponseDto registerFetch(FetchRegisterRequestDto registerFetchRequestDto, HttpServletRequest request);
    void deleteFetch(Long fetchId, HttpServletRequest request);

    List<FetchGetResponseDto> getFetch(HttpServletRequest request);
}
