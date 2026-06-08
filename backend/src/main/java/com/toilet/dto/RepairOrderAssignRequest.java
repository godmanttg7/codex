package com.toilet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RepairOrderAssignRequest {
    @NotBlank
    private String assigneeName;
}
