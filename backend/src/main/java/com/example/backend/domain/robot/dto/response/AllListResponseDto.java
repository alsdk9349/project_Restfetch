package com.example.backend.domain.robot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllListResponseDto {

    private List<FetchGetResponseDto> fetches;
    private List<ObserverGetResponseDto> observers;

}
