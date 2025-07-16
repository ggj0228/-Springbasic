package com.beyond.basic.b2_bold.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CommonErrorDto {
    private int status_code;
    private String status_message;
}
