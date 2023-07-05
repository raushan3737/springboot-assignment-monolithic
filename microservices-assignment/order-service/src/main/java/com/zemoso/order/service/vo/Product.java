package com.zemoso.order.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private int quantity;
}
