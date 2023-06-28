package com.zemoso.easycart.service.impl;


import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.repository.OrderRepository;
import com.zemoso.easycart.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    void convertOrderToDTO_ReturnsCorrectDTO() {
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(BigDecimal.valueOf(100.0));

        OrderDTO expectedDTO = modelMapper.map(order, OrderDTO.class);

        OrderDTO actualDTO = orderService.convertOrderToDTO(order);

        assertEquals(expectedDTO, actualDTO);
    }

    @Test
    void convertOrderToEntity_ReturnsCorrectEntity() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setTotalAmount(BigDecimal.valueOf(100.0));

        Order expectedEntity = modelMapper.map(orderDTO, Order.class);

        Order actualEntity = orderService.convertOrderToEntity(orderDTO);

        assertEquals(expectedEntity, actualEntity);
    }

    @Test
    void findAll_ReturnsAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> actualOrders = orderService.findAll();

        assertEquals(orders, actualOrders);
    }

    @Test
    void findById_ExistingId_ReturnsOrder() {
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order actualOrder = orderService.findById(orderId);

        assertEquals(order, actualOrder);
    }

    @Test
    void findById_NonExistingId_ReturnsNull() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Order actualOrder = orderService.findById(orderId);

        assertNull(actualOrder);
    }

    @Test
    void save_ReturnsSavedOrder() {
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.save(order);

        assertEquals(order, savedOrder);
    }

    @Test
    void deleteById_DeletesOrder() {
        Long orderId = 1L;

        orderService.deleteById(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
