package com.example.backend.domain.pick.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Data
public class PickGetRequestDto {

    private String fetchSerialNumber;

}
