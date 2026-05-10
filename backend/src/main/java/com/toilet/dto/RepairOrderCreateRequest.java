package com.toilet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RepairOrderCreateRequest {
    @NotNull
    private Long toiletId;
    @NotBlank
    private String faultDesc;
    @NotBlank
    private String reporter;
}
