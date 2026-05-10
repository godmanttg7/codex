package com.toilet.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Feedback {
    private Long id;
    private String toiletName;
    private String area;
    private Integer cleanlinessScore;
    private Integer queueScore;
    private Integer odorScore;
    private String comment;
    private LocalDateTime visitedAt;
    private LocalDateTime createdAt;
}
