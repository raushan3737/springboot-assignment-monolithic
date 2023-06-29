package com.zemoso.easycart.service.impl;

import com.zemoso.easycart.dto.ProductDTO;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductServiceImpl productService = new ProductServiceImpl(productRepository);
    private static final ModelMapper modelMapper = new ModelMapper();

    @Test
    void testConvertProductToDTO() {
        // Create a test product
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(9.99));

        // Convert the product to DTO using the service method
        ProductDTO productDTO = productService.convertProductToDTO(product);

        // Verify the DTO properties
        assertNotNull(productDTO);
        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getPrice(), productDTO.getPrice());
    }

    @Test
    void testConvertProductToEntity() {
        // Create a test product DTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setPrice(BigDecimal.valueOf(9.99));

        // Convert the DTO to entity using the service method
        Product product = productService.convertProductToEntity(productDTO);

        // Verify the entity properties
        assertNotNull(product);
        assertEquals(productDTO.getId(), product.getId());
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getPrice(), product.getPrice());
    }

    @Test
    void testFindAll() {
        // Create a list of test products
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        // Mock the product repository to return the test products
        when(productRepository.findAll()).thenReturn(products);

        // Call the service method to find all products
        List<Product> result = productService.findAll();

        // Verify the returned products
        assertNotNull(result);
        assertEquals(products.size(), result.size());
    }

    @Test
    void testFindById_ReturnsProduct_WhenProductExists() {
        // Create a test product
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(9.99));

        // Mock the product repository to return the test product
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Call the service method to find the product by ID
        Optional<Product> result = productService.findById(1L);

        // Verify the returned product
        assertTrue(result.isPresent());
        assertEquals(product.getId(), result.get().getId());
        assertEquals(product.getName(), result.get().getName());
        assertEquals(product.getPrice(), result.get().getPrice());
    }

    @Test
    void testFindById_ReturnsEmptyOptional_WhenProductDoesNotExist() {
        // Mock the product repository to return an empty optional
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the service method to find the product by ID
        Optional<Product> result = productService.findById(1L);

        // Verify the returned optional is empty
        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        // Create a test product
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(9.99));

        // Mock the product repository to return the test product
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call the service method to save the product
        Product result = productService.save(product);

        // Verify the saved product
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
    }


    @Test
    void testValidateProducts_ReturnsTrue_WhenAllProductsExist() {
        // Create a list of test products
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", "Desc 1", BigDecimal.valueOf(9.99)));
        products.add(new Product(2L, "Product 2", "Desc 2", BigDecimal.valueOf(19.99)));

        // Mock the product repository to return existing products for all IDs
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new Product(1L, "Product 1", "Desc 1", BigDecimal.valueOf(9.99))));
        when(productRepository.findById(2L)).thenReturn(Optional.of(
                new Product(2L, "Product 2", "Desc 2", BigDecimal.valueOf(19.99))));

        // Call the service method to validate the products
        boolean result = productService.validateProducts(products);

        // Verify the result is true
        assertTrue(result);
    }


    @Test
    void testValidateProducts_ReturnsFalse_WhenAnyProductDoesNotExist() {
        // Create a list of test products
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", "Desc 1", BigDecimal.valueOf(9.99)));
        products.add(new Product(2L, "Product 2", "Desc 2", BigDecimal.valueOf(19.99)));

        // Mock the product repository to return non-existing products for some IDs
        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new Product(1L, "Product 1", "Desc 1", BigDecimal.valueOf(9.99))));

        when(productRepository.findById(1L)).thenReturn(Optional.of(
                new Product(2L, "Product 2", "Desc 2", BigDecimal.valueOf(19.99))));

        // Call the service method to validate the products
        boolean result = productService.validateProducts(products);

        // Verify the result is false
        assertFalse(result);

    }


    @Test
    void testValidateProducts_ReturnsFalse_WhenAtLeastOneProductDoesNotExist() {
        // Create a list of test products
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());

        // Mock the product repository to return an empty optional for the second ID
        when(productRepository.findById(eq(2L))).thenReturn(Optional.empty());

        // Call the service method to validate the products
        boolean result = productService.validateProducts(products);

        // Verify the result is false
        assertFalse(result);
    }

    @Test
    void testGetTotalAmountOfProducts() {
        // Create a list of test products
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", "Desc 1", BigDecimal.valueOf(9.99)));
        products.add(new Product(2L, "Product 2", "Desc 2", BigDecimal.valueOf(19.99)));

        // Mock the product repository to return the test products for all IDs
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Product(1L, "Product 1", "Desc 1", BigDecimal.valueOf(9.99))))
                .thenReturn(Optional.of(new Product(2L, "Product 2", "Desc 2", BigDecimal.valueOf(19.99))));

        // Call the service method to calculate the total amount
        BigDecimal result = productService.getTotalAmountOfProducts(products);

        // Calculate the expected total amount
        BigDecimal expectedTotalAmount = BigDecimal.valueOf(9.99).add(BigDecimal.valueOf(19.99));

        // Verify the calculated total amount
        assertNotNull(result);
        assertEquals(expectedTotalAmount, result);
    }
}

