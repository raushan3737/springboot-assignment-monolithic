package com.zemoso.easycart.dto;

import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.entity.User;
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

    private User user = new User();

    private List<Product> products = new ArrayList<>();

    private BigDecimal totalAmount;

    private String createdAt;

}
