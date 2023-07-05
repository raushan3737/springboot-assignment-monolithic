package com.zemoso.order.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;

    private Long userId;

    private List<Long> productIds = new ArrayList<>();

    private BigDecimal totalAmount;

    private String createdAt;

    private int quantity;
}


