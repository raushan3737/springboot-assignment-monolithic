package com.zemoso.easycart.controller;

import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.service.OrderService;
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
        List<Order> orders = orderService.findAll();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(orderService::convertOrderToDTO)
                .collect(Collectors.toList());

        if (!orderDTOs.isEmpty()) {
            return ResponseEntity.ok(orderDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        Optional<Order> orderOptional = Optional.ofNullable(orderService.findById(orderId));

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            OrderDTO orderDTO = orderService.convertOrderToDTO(order);
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new OrderDTO());
        }
    }


    @PostMapping("/")
    public ResponseEntity<Object> addOrder(@RequestBody OrderDTO orderDTO) {

        ResponseEntity<OrderDTO> res = orderService.addOrder(orderDTO);
        if (res != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    @DeleteMapping("/{orderId}")
    public String deleteOrderById(@PathVariable Long orderId) {
        Order order = orderService.findById(orderId);
        if (order != null) {
            orderService.deleteById(orderId);
            return "Order deleted with id: " + order.getId();
        } else {
            return "Order not found with id: " + orderId;
        }
    }

}