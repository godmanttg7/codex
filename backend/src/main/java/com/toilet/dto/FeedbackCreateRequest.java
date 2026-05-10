package com.toilet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackCreateRequest {
    @NotBlank
    private String toiletName;
    @NotBlank
    private String area;
    @NotNull @Min(1) @Max(5)
    private Integer cleanlinessScore;
    @NotNull @Min(1) @Max(5)
    private Integer queueScore;
    @NotNull @Min(1) @Max(5)
    private Integer odorScore;
    private String comment;
    @NotNull
    private LocalDateTime visitedAt;
}
