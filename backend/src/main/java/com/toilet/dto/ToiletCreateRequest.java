package com.toilet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ToiletCreateRequest {
    @NotBlank
    private String toiletCode;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    private String district;
    private String openTime;
}
