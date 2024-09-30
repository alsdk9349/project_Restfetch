package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.ObserverRegisterResponseDto;

public interface ObserverService {

    ObserverRegisterResponseDto registerObserver(Long fetchId, ObserverRegisterRequestDto observerRegisterRequestDto);
    void deleteObserver(Long fetchId, Long observerId);
}
