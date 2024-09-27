package com.example.backend.domain.fetch.dto.request;

import com.example.backend.domain.fetch.entity.Observer;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FetchRegisterRequestDto {

    @NotNull(message = "시리얼 번호를 입력해주세요.")
    private String fetchSerialNumber;

    private String nickname;


}
