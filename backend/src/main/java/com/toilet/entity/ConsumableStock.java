package com.toilet.entity;

import lombok.Data;

@Data
public class ConsumableStock {
    private Long id;
    private Long toiletId;
    private String toiletName;
    private String consumableName;
    private Integer currentStock;
    private Integer minStock;
    private Boolean lowStock;
}
