package com.zemoso.order.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsVO {

    private Long orderId;

    private User user;

    private Set<Product> product = new HashSet<>();

    private BigDecimal totalAmount;

    private String createdAt;

}
