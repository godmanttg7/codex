package com.toilet.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Long id;
    private String receiverName;
    private String title;
    private String content;
    private Boolean readFlag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
