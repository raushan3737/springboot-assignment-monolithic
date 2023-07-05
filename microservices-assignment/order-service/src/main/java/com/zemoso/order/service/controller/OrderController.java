package com.zemoso.order.service.controller;

import com.zemoso.order.service.dto.OrderDTO;
import com.zemoso.order.service.entity.Order;
import com.zemoso.order.service.exception.OrderNotFoundException;
import com.zemoso.order.service.exception.ProductNotFoundException;
import com.zemoso.order.service.exception.UserNotFoundException;
import com.zemoso.order.service.service.OrderService;
import com.zemoso.order.service.vo.OrderDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(orderService::convertOrderToDTO)
                    .collect(Collectors.toList());

            if (!orderDTOs.isEmpty()) {
                return ResponseEntity.ok(orderDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.emptyList());
            }
        } catch (Exception e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDetailsVO> getOrderDetailsById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetailsById(orderId));
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<Order> orderOptional = Optional.ofNullable(orderService.getOrderById(orderId));

            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                OrderDTO orderDTO = orderService.convertOrderToDTO(order);
                return ResponseEntity.ok(orderDTO);
            } else {
                throw new OrderNotFoundException("Order not found with ID: " + orderId);
            }
        } catch (OrderNotFoundException e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderDTO());
        } catch (Exception e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO res = orderService.addOrder(orderDTO);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (UserNotFoundException | ProductNotFoundException e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long orderId) {
        try {
            String result = orderService.deleteOrderById(orderId);
            return ResponseEntity.ok(result);
        } catch (OrderNotFoundException e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // Handle and log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
