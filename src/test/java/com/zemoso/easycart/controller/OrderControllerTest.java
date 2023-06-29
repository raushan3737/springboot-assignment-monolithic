package com.zemoso.easycart.controller;

import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders_ReturnsOrderDTOList_WhenOrdersExist() {
        // Mock order data
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        // Mock orderService.findAll() to return the list of orders
        when(orderService.findAll()).thenReturn(orders);

        // Call the controller method
        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllOrders_ReturnsEmptyList_WhenNoOrdersExist() {
        // Mock orderService.findAll() to return an empty list
        when(orderService.findAll()).thenReturn(new ArrayList<>());

        // Call the controller method
        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetOrderById_ReturnsOrderDTO_WhenOrderExists() {
        // Mock order data
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        // Mock orderService.findById() to return the order
        when(orderService.findById(orderId)).thenReturn(order);

        // Call the controller method
        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testGetOrderById_ReturnsEmptyOrderDTO_WhenOrderDoesNotExist() {
        // Mock orderService.findById() to return null
        when(orderService.findById(anyLong())).thenReturn(null);

        // Call the controller method
        ResponseEntity<OrderDTO> response = orderController.getOrderById(1L);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void testAddOrder_ReturnsCreatedOrderDTO_WhenOrderAddedSuccessfully() {
        // Mock order data
        OrderDTO orderDTO = new OrderDTO();
        OrderDTO savedOrderDTO = new OrderDTO();
        savedOrderDTO.setId(1L);

        // Mock orderService.addOrder() to return the saved order
        when(orderService.addOrder(any(OrderDTO.class))).thenReturn(ResponseEntity.ok(savedOrderDTO));

        // Call the controller method
        ResponseEntity<Object> response = orderController.addOrder(orderDTO);


        // Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    void testAddOrder_ReturnsBadRequest_WhenOrderNotAdded() {
        // Mock orderService.addOrder() to return null
        when(orderService.addOrder(any(OrderDTO.class))).thenReturn(null);

        // Call the controller method
        ResponseEntity<Object> response = orderController.addOrder(new OrderDTO());

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteOrderById_ReturnsSuccessMessage_WhenOrderExists() {
        // Mock order data
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        // Mock orderService.findById() to return the order
        when(orderService.findById(orderId)).thenReturn(order);

        // Call the controller method
        String response = orderController.deleteOrderById(orderId);

        // Verify the response
        assertEquals("Order deleted with id: " + orderId, response);
    }

    @Test
    void testDeleteOrderById_ReturnsNotFoundMessage_WhenOrderDoesNotExist() {
        // Mock orderService.findById() to return null
        when(orderService.findById(anyLong())).thenReturn(null);

        // Call the controller method
        String response = orderController.deleteOrderById(1L);

        // Verify the response
        assertEquals("Order not found with id: 1", response);
    }
}
