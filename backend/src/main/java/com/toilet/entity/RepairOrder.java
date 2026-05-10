package com.toilet.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepairOrder {
    private Long id;
    private Long toiletId;
    private String toiletName;
    private String faultDesc;
    private String reporter;
    private String status;
    private LocalDateTime createdAt;
}
