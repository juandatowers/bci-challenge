package com.bci.challenge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneDto {

    private long number;
    private int cityCode;
    private String countryCode;
}
