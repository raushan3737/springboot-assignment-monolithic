package com.zemoso.easycart.repository;

import com.zemoso.easycart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
