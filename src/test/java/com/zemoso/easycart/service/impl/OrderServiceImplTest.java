package com.zemoso.easycart.service.impl;


import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.repository.OrderRepository;
import com.zemoso.easycart.service.ProductService;
import com.zemoso.easycart.service.UserService;
import com.zemoso.easycart.utils.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private DateFormatter dateFormatter;

    @InjectMocks
    private OrderServiceImpl orderService;

    private static final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertOrderToDTO_ReturnsCorrectOrderDTO() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(BigDecimal.valueOf(500.00));

        // Act
        OrderDTO orderDTO = orderService.convertOrderToDTO(order);

        // Assert
        assertEquals(order.getId(), orderDTO.getId());
        assertEquals(order.getTotalAmount(), orderDTO.getTotalAmount());
    }

    @Test
    void convertOrderToEntity_ReturnsCorrectOrderEntity() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setTotalAmount(BigDecimal.valueOf(500.00));

        // Act
        Order order = orderService.convertOrderToEntity(orderDTO);

        // Assert
        assertEquals(orderDTO.getId(), order.getId());
        assertEquals(orderDTO.getTotalAmount(), order.getTotalAmount());
    }

    @Test
    void findAll_ReturnsListOfOrders() {
        // Arrange
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order());
        orderList.add(new Order());
        when(orderRepository.findAll()).thenReturn(orderList);

        // Act
        List<Order> result = orderService.findAll();

        // Assert
        assertEquals(orderList.size(), result.size());
    }

    @Test
    void findById_WithValidId_ReturnsOrder() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.findById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void findById_WithInvalidId_ReturnsNull() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Order result = orderService.findById(orderId);

        // Assert
        assertNull(result);
    }

    @Test
    void save_ReturnsSavedOrder() {
        // Arrange
        Order order = new Order();
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order result = orderService.save(order);

        // Assert
        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void deleteById_CallsOrderRepositoryDeleteById() {
        // Arrange
        Long orderId = 1L;

        // Act
        orderService.deleteById(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void validateUser_WithValidUserId_ReturnsTrue() {
        // Arrange
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(Optional.of(new User()));

        // Act
        boolean result = orderService.validateUser(userId);

        // Assert
        assertTrue(result);
    }

    @Test
    void validateUser_WithInvalidUserId_ReturnsFalse() {
        // Arrange
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = orderService.validateUser(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void addOrder_WithValidData_ReturnsCreatedResponse() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUser(user);
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        orderDTO.setProducts(products);
        BigDecimal totalAmount = BigDecimal.valueOf(500.00);
        Order order = new Order();
        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(productService.validateProducts(products)).thenReturn(true);
        when(productService.getTotalAmountOfProducts(products)).thenReturn(totalAmount);
        when(dateFormatter.formatToIndiaTimeZone(any())).thenReturn("");
        when(orderRepository.save(any())).thenReturn(order);
        OrderDTO expectedOrderDTO = modelMapper.map(order, OrderDTO.class);

        // Act
        ResponseEntity<OrderDTO> response = orderService.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedOrderDTO, response.getBody());
    }

    @Test
    void addOrder_WithInvalidUser_ReturnsBadRequestResponse() {
        // Arrange
        Long userId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        User user = new User();
        user.setId(userId);
        orderDTO.setUser(user);
        when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<OrderDTO> response = orderService.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addOrder_WithInvalidProducts_ReturnsBadRequestResponse() {
        // Arrange
        Long userId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        User user = new User();
        user.setId(userId);
        orderDTO.setUser(user);
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        orderDTO.setProducts(products);
        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(productService.validateProducts(products)).thenReturn(false);

        // Act
        ResponseEntity<OrderDTO> response = orderService.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addOrder_WithException_ReturnsInternalServerErrorResponse() {
        // Arrange
        Long userId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        User user = new User();
        user.setId(userId);
        orderDTO.setUser(user);
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        orderDTO.setProducts(products);
        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(productService.validateProducts(products)).thenReturn(true);
        when(productService.getTotalAmountOfProducts(products)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<OrderDTO> response = orderService.addOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

}