package com.zemoso.order.service.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class OrderTest {

    private Order order;
    private User user;
    private List<Product> products;

    @BeforeEach
    public void setup() {
        // Create a sample user
        user = new User();
        user.setId(1L);
        user.setUsername("raushan");
        user.setEmail("raushan@example.com");
        user.setPassword("password123");

        // Create a list of sample products
        products = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(10.0));
        products.add(product1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(20.0));
        products.add(product2);

        // Create a new order
        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setProducts(products);
        order.setTotalAmount(BigDecimal.valueOf(30.0));
        order.setCreatedAt("2023-06-23T10:00:00");
    }

    @Test
    void testOrderEntity() {
        // Verify the order properties
        Assertions.assertEquals(1L, order.getId());
        Assertions.assertEquals(user, order.getUser());
        Assertions.assertEquals(products, order.getProducts());
        Assertions.assertEquals(BigDecimal.valueOf(30.0), order.getTotalAmount());
        Assertions.assertEquals("2023-06-23T10:00:00", order.getCreatedAt());
    }

    @Test
    void testOrderEntityWithMissingProperties() {
        // Create a new order without setting any properties
        Order emptyOrder = new Order();

        // Verify that the properties are null or have default values
        Assertions.assertNull(emptyOrder.getId());
        Assertions.assertNull(emptyOrder.getUser());
        Assertions.assertNull(emptyOrder.getProducts());
        Assertions.assertNull(emptyOrder.getTotalAmount());
        Assertions.assertNull(emptyOrder.getCreatedAt());
    }

    @Test
    void testOrderEntityWithEmptyProductList() {
        // Create a new order with an empty product list
        Order orderWithEmptyProducts = new Order();
        orderWithEmptyProducts.setId(2L);
        orderWithEmptyProducts.setUser(user);
        orderWithEmptyProducts.setProducts(new ArrayList<>());
        orderWithEmptyProducts.setTotalAmount(BigDecimal.ZERO);
        orderWithEmptyProducts.setCreatedAt("2023-06-24T09:00:00");

        // Verify that the order has an empty product list and zero total amount
        Assertions.assertEquals(2L, orderWithEmptyProducts.getId());
        Assertions.assertEquals(user, orderWithEmptyProducts.getUser());
        Assertions.assertTrue(orderWithEmptyProducts.getProducts().isEmpty());
        Assertions.assertEquals(BigDecimal.ZERO, orderWithEmptyProducts.getTotalAmount());
        Assertions.assertEquals("2023-06-24T09:00:00", orderWithEmptyProducts.getCreatedAt());
    }
}
