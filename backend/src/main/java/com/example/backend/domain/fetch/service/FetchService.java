package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.FetchRegisterResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface FetchService {

    FetchRegisterResponseDto registerFetch(FetchRegisterRequestDto registerFetchRequestDto, HttpServletRequest request);
    void deleteFetch(Long fetchId, HttpServletRequest request);
}
