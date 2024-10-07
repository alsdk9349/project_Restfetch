package com.example.backend.domain.robot.service;

import com.example.backend.domain.robot.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.robot.dto.response.ObserverGetResponseDto;
import com.example.backend.domain.robot.dto.response.ObserverRegisterResponseDto;

import java.util.List;

public interface ObserverService {

    ObserverRegisterResponseDto registerObserver(ObserverRegisterRequestDto observerRegisterRequestDto);
    void deleteObserver(Long fetchId, Long observerId);

    List<ObserverGetResponseDto> getObserver(Long fetchId);
}
