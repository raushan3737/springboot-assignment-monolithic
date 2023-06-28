package com.zemoso.easycart.controller;

import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.service.OrderService;
import com.zemoso.easycart.service.ProductService;
import com.zemoso.easycart.service.UserService;
import com.zemoso.easycart.utils.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {


    private DateFormatter dateFormatter;
    private final OrderService orderService;

    private final UserService userService;

    private final ProductService productService;


    @Autowired
    public OrderController(DateFormatter dateFormatter,
                           OrderService orderService, UserService userService, ProductService productService) {

        this.dateFormatter = dateFormatter;
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> orderService.convertOrderToDTO(order))
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
        Long userId = orderDTO.getUser().getId();
        Optional<User> user = userService.findById(userId);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<Product> products = orderDTO.getProducts();
        if (!productService.validateProducts(products)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid products");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Product product : products) {
            Optional<Product> productOptional = productService.findById(product.getId());
            if (productOptional.isPresent()) {
                BigDecimal productPrice = productOptional.get().getPrice();
                totalAmount = totalAmount.add(productPrice);
            }
        }

        Order order = orderService.convertOrderToEntity(orderDTO);
        order.setId(null); // Setting to null instead of 0L

        Date currentDate = new Date();
        order.setCreatedAt(dateFormatter.formatToIndiaTimeZone(currentDate));
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderService.save(order);
        OrderDTO savedOrderDTO = orderService.convertOrderToDTO(savedOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderDTO);
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