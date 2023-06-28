package com.zemoso.easycart.controller;


import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.service.OrderService;
import com.zemoso.easycart.service.ProductService;
import com.zemoso.easycart.service.UserService;
import com.zemoso.easycart.utils.DateFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private DateFormatter dateFormatter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        // Arrange
        List<Order> orders = Collections.singletonList(new Order());
        when(orderService.findAll()).thenReturn(orders);

        // Act
        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders.size(), response.getBody().size());
        verify(orderService, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.findById(orderId)).thenReturn(order);
        when(orderService.convertOrderToDTO(order)).thenReturn(new OrderDTO());

        // Act
        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(orderService, times(1)).findById(orderId);
        verify(orderService, times(1)).convertOrderToDTO(order);
    }


    @Test
    void testGetOrderById_OrderNotFound() {
        // Arrange
        Long orderId = 1L;
        when(orderService.findById(orderId)).thenReturn(null);

        // Act
        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new OrderDTO(), response.getBody());
        verify(orderService, times(1)).findById(orderId);
    }

    @Test
    void testAddOrder() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        User user = new User();
        orderDTO.setUser(user);
        List<Product> products = Collections.singletonList(new Product());
        orderDTO.setProducts(products);
        Order order = new Order();
        when(userService.findById(user.getId())).thenReturn(Optional.of(user));
        when(productService.validateProducts(products)).thenReturn(true);
        when(orderService.convertOrderToEntity(orderDTO)).thenReturn(order);
        when(orderService.save(order)).thenReturn(order);
        when(orderService.convertOrderToDTO(order)).thenReturn(orderDTO);

        // Act
        ResponseEntity<Object> response = orderController.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderDTO, response.getBody());
        verify(userService, times(1)).findById(user.getId());
        verify(productService, times(1)).validateProducts(products);
        verify(orderService, times(1)).convertOrderToEntity(orderDTO);
        verify(orderService, times(1)).save(order);
        verify(orderService, times(1)).convertOrderToDTO(order);
    }

    @Test
    void testAddOrder_UserNotFound() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        User user = new User();
        orderDTO.setUser(user);
        Long userId = user.getId();
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> response = orderController.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).findById(userId);
        verifyNoInteractions(productService);
        verifyNoInteractions(orderService);
    }

    @Test
    void testAddOrder_InvalidProducts() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        User user = new User();
        orderDTO.setUser(user);
        List<Product> products = Collections.singletonList(new Product());
        orderDTO.setProducts(products);
        Long userId = user.getId();
        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(productService.validateProducts(products)).thenReturn(false);

        // Act
        ResponseEntity<Object> response = orderController.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid products", response.getBody());
        verify(userService, times(1)).findById(userId);
        verify(productService, times(1)).validateProducts(products);
        verifyNoInteractions(orderService);
    }

    @Test
    void testDeleteOrderById() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.findById(orderId)).thenReturn(order);

        // Act
        String response = orderController.deleteOrderById(orderId);

        // Assert
        Assertions.assertTrue(response.contains("Order deleted"));
        verify(orderService, times(1)).findById(orderId);
        verify(orderService, times(1)).deleteById(orderId);
    }


}
