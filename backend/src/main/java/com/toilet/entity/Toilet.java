package com.toilet.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Toilet {
    private Long id;
    private String toiletCode;
    private String name;
    private String address;
    private String district;
    private String openTime;
    private LocalDateTime createdAt;
}
