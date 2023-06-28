package com.zemoso.easycart.service;

import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    public OrderDTO convertOrderToDTO(Order order);

    public Order convertOrderToEntity(OrderDTO orderDTO);

    public List<Order> findAll();

    public Order findById(Long theId);

    public Order save(Order order);

    public void deleteById(Long theId);


}
