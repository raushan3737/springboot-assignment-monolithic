package com.zemoso.product.service.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
    }

    @Test
    public void testProductId() {
        // Set and verify product ID
        product.setId(1L);
        Assertions.assertEquals(1L, product.getId());
    }

    @Test
    public void testProductName() {
        // Set and verify product name
        product.setName("iPhone 12");
        Assertions.assertEquals("iPhone 12", product.getName());
    }

    @Test
    public void testProductDescription() {
        // Set and verify product description
        product.setDescription("The latest iPhone model.");
        Assertions.assertEquals("The latest iPhone model.", product.getDescription());
    }

    @Test
    public void testProductPrice() {
        // Set and verify product price
        BigDecimal price = new BigDecimal("999.99");
        product.setPrice(price);
        Assertions.assertEquals(price, product.getPrice());
    }

    @Test
    public void testProductDefaultValues() {
        // Verify default values of properties
        Assertions.assertNull(product.getId());
        Assertions.assertNull(product.getName());
        Assertions.assertNull(product.getDescription());
        Assertions.assertNull(product.getPrice());
    }
}
