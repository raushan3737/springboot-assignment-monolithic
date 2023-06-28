package com.zemoso.easycart.service.impl;

import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.repository.OrderRepository;
import com.zemoso.easycart.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private static final ModelMapper modelMapper = new ModelMapper();


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public OrderDTO convertOrderToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public Order convertOrderToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long theId) {
        Optional<Order> orderOptional = orderRepository.findById(theId);
        return orderOptional.orElse(null);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteById(Long theId) {
        orderRepository.deleteById(theId);
    }

}
