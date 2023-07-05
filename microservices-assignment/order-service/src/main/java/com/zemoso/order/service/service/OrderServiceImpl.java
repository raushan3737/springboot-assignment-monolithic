package com.zemoso.order.service.service;

import com.zemoso.order.service.dto.OrderDTO;
import com.zemoso.order.service.entity.Order;
import com.zemoso.order.service.exception.OrderNotFoundException;
import com.zemoso.order.service.exception.ProductNotFoundException;
import com.zemoso.order.service.exception.QuantityZeroException;
import com.zemoso.order.service.exception.UserNotFoundException;
import com.zemoso.order.service.repository.OrderRepository;
import com.zemoso.order.service.utils.DateFormatter;
import com.zemoso.order.service.vo.OrderDetailsVO;
import com.zemoso.order.service.vo.Product;
import com.zemoso.order.service.vo.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private static final ModelMapper modelMapper = new ModelMapper();

    private final DateFormatter dateFormatter;

    private static final String USER_SERVICE_URL = "http://USER-SERVICE/users/";
    private static final String PRODUCT_SERVICE_URL = "http://PRODUCT-SERVICE/products/";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            RestTemplate restTemplate, DateFormatter dateFormatter) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
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
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        try {
            // Validation of user and products
            Map<Boolean, Long> res = getTotalAmountOfProducts(orderDTO);

            boolean isValid = res.containsKey(true) && res.get(true) > 0;

            if (validateUserById(orderDTO.getUserId()) && isValid) {
                // Check if the product quantities are zero
                boolean isQuantityZero = false;
                for (Long productId : orderDTO.getProductIds()) {
                    int quantity = getProductQuantity(productId);
                    if (quantity == 0) {
                        isQuantityZero = true;
                        break;
                    }
                }

                if (isQuantityZero) {
                    // Handle the case when the quantity is zero
                    throw new QuantityZeroException("Cannot add order with zero quantity for a product");
                }

                Order order = new Order();
                order.setId(null);
               // Setting timestamp for the order
                Date currentDate = new Date();
                order.setCreatedAt(dateFormatter.formatToIndiaTimeZone(currentDate));

                order.setUserId(orderDTO.getUserId());
                order.setProductIds(orderDTO.getProductIds());
                order.setTotalAmount(BigDecimal.valueOf(res.get(true)));
                order.setQuantity(getUniqueProductCount(orderDTO.getProductIds())); // Set the quantity based on the unique number of products
                orderRepository.save(order);

                // Update the product quantities
                for (Long productId : orderDTO.getProductIds()) {
                    updateProductQuantity(productId, -1); // Decrease the quantity by 1
                }

                return convertOrderToDTO(order);


            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding order: " + e.getMessage());
        }

        return null;
    }


    @Override
    public String deleteOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            orderRepository.deleteById(orderId);
            return "Order deleted with id: " + orderId;
        } else {
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        }
    }

    @Override
    public OrderDetailsVO getOrderDetailsById(Long orderId) {

        try {
            Order order = getOrderById(orderId);

            // Check if the order exists
            if (order == null) {
                // Handle the case when the order does not exist
                throw new OrderNotFoundException("Order not found for ID: " + orderId);
            }

            // Get all product details
            Set<Product> productDetailList = getProductDetailList(order.getProductIds());

            OrderDetailsVO orderDetails = new OrderDetailsVO();
            orderDetails.setOrderId(orderId);
            orderDetails.setUser(getUserById(order.getUserId()));
            orderDetails.setProduct(productDetailList);
            orderDetails.setTotalAmount(order.getTotalAmount());
            orderDetails.setCreatedAt(order.getCreatedAt());

            return orderDetails;
        } catch (OrderNotFoundException e) {
            throw new OrderNotFoundException("Order not found for the given order ID..");
        }

    }


    public Set<Product> getProductDetailList(List<Long> productIds) {
        Set<Product> productList = new HashSet<>();

        for (Long productId : productIds) {
            productList.add(getProductById(productId));
        }
        return productList;
    }


    boolean validateUserById(Long userId) {
        // Call to the USER-MICROSERVICE
        User userDTO = restTemplate.getForObject(USER_SERVICE_URL + userId, User.class);
        if (userDTO == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return true;
    }


    public User getUserById(Long userId) {
        // Get the user from the USER-SERVICE
        User userDTO = restTemplate.getForObject(USER_SERVICE_URL + userId, User.class);
        if (userDTO == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        return userDTO;
    }


    public Product getProductById(Long productId) {
        // Get the user from the PRODUCT-SERVICE
        Product productDTO = restTemplate.getForObject(PRODUCT_SERVICE_URL + productId, Product.class);
        if (productDTO == null) {
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
        return productDTO;
    }

    Map<Boolean, Long> getTotalAmountOfProducts(OrderDTO orderDTO) {
        Map<Boolean, Long> res = new HashMap<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        List<Long> productIds = orderDTO.getProductIds();

        for (Long productId : productIds) {
            if (validateProductById(productId)) {
                Product productDTO = getProductById(productId);

                System.out.println("Product id: " + productId + " : " + productDTO.getPrice());
                totalPrice = totalPrice.add(productDTO.getPrice());
            } else {
                res.put(false, 0L);
                return res;
            }
        }
        res.put(true, totalPrice.longValue());
        return res;
    }

    boolean validateProductById(Long productId) {
        // Call to the PRODUCT-MICROSERVICE
        Product productDTO = restTemplate.getForObject(PRODUCT_SERVICE_URL + productId, Product.class);
        return productDTO != null;
    }


    public void updateProductQuantity(Long productId, int quantity) {
        // Get the product from the PRODUCT-SERVICE
        Product productDTO = getProductById(productId);
        // Update the quantity
        productDTO.setQuantity(productDTO.getQuantity() + quantity); // Add the updated quantity
        // Update the product in the PRODUCT-SERVICE
        restTemplate.put(PRODUCT_SERVICE_URL + productId, productDTO);
    }


    public int getProductQuantity(Long productId) {
        Product product = getProductById(productId);
        return product.getQuantity();
    }

    private int getUniqueProductCount(List<Long> productIds) {
        Set<Long> uniqueProductIds = new HashSet<>(productIds);
        return uniqueProductIds.size();
    }


}
