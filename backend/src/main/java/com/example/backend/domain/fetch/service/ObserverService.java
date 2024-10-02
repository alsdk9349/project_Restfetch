package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.FetchGetResponseDto;
import com.example.backend.domain.fetch.dto.response.ObserverGetResponseDto;
import com.example.backend.domain.fetch.dto.response.ObserverRegisterResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ObserverService {

    ObserverRegisterResponseDto registerObserver(Long fetchId, ObserverRegisterRequestDto observerRegisterRequestDto);
    void deleteObserver(Long fetchId, Long observerId);

    List<ObserverGetResponseDto> getObserver(Long fetchId);
}
