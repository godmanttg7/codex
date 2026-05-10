package com.toilet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RepairOrderStatusRequest {
    @NotBlank
    private String status;
}
