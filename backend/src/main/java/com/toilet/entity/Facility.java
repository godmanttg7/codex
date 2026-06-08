package com.toilet.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Facility {
    private Long id;
    private Long toiletId;
    private String toiletName;
    private String facilityName;
    private String facilityType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
