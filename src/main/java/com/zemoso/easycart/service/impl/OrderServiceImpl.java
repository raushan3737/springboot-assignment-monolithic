package com.zemoso.easycart.service.impl;

import com.zemoso.easycart.dto.OrderDTO;
import com.zemoso.easycart.entity.Order;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.exception.CustomException;
import com.zemoso.easycart.repository.OrderRepository;
import com.zemoso.easycart.service.OrderService;
import com.zemoso.easycart.service.ProductService;
import com.zemoso.easycart.service.UserService;
import com.zemoso.easycart.utils.DateFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    private final UserService userService;

    private final ProductService productService;

    private final DateFormatter dateFormatter;

    private static final ModelMapper modelMapper = new ModelMapper();


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService,
                            ProductService productService, DateFormatter dateFormatter) {

        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.dateFormatter = dateFormatter;
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


    // Function to validate the user
    boolean validateUser(Long userId) {
        return userService.findById(userId).isPresent();
    }

    @Override
    public ResponseEntity<OrderDTO> addOrder(OrderDTO orderDTO) {
        try {
            Long userId = orderDTO.getUser().getId();

            if (!validateUser(userId)) {
                throw new CustomException("Invalid userId: " + userId);
            }

            // Validation of products
            List<Product> products = orderDTO.getProducts();
            if (!productService.validateProducts(products)) {
                throw new CustomException("Invalid productId");
            }

            BigDecimal totalAmount = productService.getTotalAmountOfProducts(products);

            Order order = convertOrderToEntity(orderDTO);
            order.setId(null); // Setting to null instead of 0L

            Date currentDate = new Date();
            order.setCreatedAt(dateFormatter.formatToIndiaTimeZone(currentDate));
            order.setTotalAmount(totalAmount);

            Order savedOrder = orderRepository.save(order);
            OrderDTO savedOrderDTO = convertOrderToDTO(savedOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrderDTO);
        } catch (CustomException e) {
            // Handle custom exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
