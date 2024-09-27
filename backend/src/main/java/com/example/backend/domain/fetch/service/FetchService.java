package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.RegisterFetchRequestDto;
import com.example.backend.domain.fetch.dto.response.RegisterFetchResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface FetchService {

    RegisterFetchResponseDto registerFetch(RegisterFetchRequestDto registerFetchRequestDto, HttpServletRequest request);
}
