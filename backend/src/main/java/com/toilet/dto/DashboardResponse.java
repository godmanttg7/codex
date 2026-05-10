package com.toilet.dto;

import lombok.Data;

@Data
public class DashboardResponse {
    private Long toiletCount;
    private Long feedbackCount;
    private Long repairPendingCount;
    private Long lowStockCount;
}
