package com.example.backend.domain.pick.service;

import com.example.backend.domain.pick.dto.request.PickGetRequestDto;
import com.example.backend.domain.pick.dto.response.PickCheckResponseDto;
import com.example.backend.domain.pick.dto.response.PickGetResponseDto;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;

public interface PickService {

    void requestPick(long reportId);
    PickGetResponseDto getPick(PickGetRequestDto pickGetRequestDto);
    PickCheckResponseDto checkPick(long reportId);
    ReportGetResponseDto pick(long reportId);
}
