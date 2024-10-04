package com.example.backend.domain.pick.service;

import com.example.backend.domain.pick.dto.response.PickCheckResponseDto;
import com.example.backend.domain.pick.dto.response.PickGetResponseDto;

public interface PickService {

    void pick(long reportId);
    PickGetResponseDto getPick();
    PickCheckResponseDto checkPick(long reportId);

}
