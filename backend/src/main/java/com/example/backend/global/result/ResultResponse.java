package com.example.backend.global.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultResponse {
    private final int status;
    private final String code;
    private final String message;
    private final Object data;

    public static ResultResponse of(ResultCode resultCode, Object data) {
        return ResultResponse.builder()
                .status(resultCode.getStatus().value())
                .code(resultCode.getCode())
                .data(data)
                .message(resultCode.getMessage())
                .build();
    }

    public static ResultResponse of(ResultCode resultCode) {
        return ResultResponse.builder()
                .status(resultCode.getStatus().value())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .build();
    }
}
