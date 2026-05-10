package com.toilet.dto;

import lombok.Data;

@Data
public class StatsResponse {
    private Long totalCount;
    private Double avgCleanliness;
    private Double avgQueue;
    private Double avgOdor;
}
