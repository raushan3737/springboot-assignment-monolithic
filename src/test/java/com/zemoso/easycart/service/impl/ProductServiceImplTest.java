package com.zemoso.easycart.service.impl;


import com.zemoso.easycart.dto.ProductDTO;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;



class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void testConvertProductToDTO() {
        // Create a Product object
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setPrice(BigDecimal.valueOf(10.0));

        // Convert Product to ProductDTO
        ProductDTO productDTO = productService.convertProductToDTO(product);

        // Verify the conversion
        Assertions.assertEquals(product.getId(), productDTO.getId());
        Assertions.assertEquals(product.getName(), productDTO.getName());
        Assertions.assertEquals(product.getPrice(), productDTO.getPrice());
    }

    @Test
    void testConvertProductToEntity() {
        // Create a ProductDTO object
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Product 1");
        productDTO.setPrice(BigDecimal.valueOf(10.0));

        // Convert ProductDTO to Product
        Product product = productService.convertProductToEntity(productDTO);

        // Verify the conversion
        Assertions.assertEquals(productDTO.getId(), product.getId());
        Assertions.assertEquals(productDTO.getName(), product.getName());
        Assertions.assertEquals(productDTO.getPrice(), product.getPrice());
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        // Arrange
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product(1L, "Product 1", "Description 1", new BigDecimal("10.00")));
        expectedProducts.add(new Product(2L, "Product 2", "Description 2", new BigDecimal("20.00")));
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.findAll();

        // Assert
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_ExistingId_ShouldReturnProduct() {
        // Arrange
        Long productId = 1L;
        Product expectedProduct = new Product(productId, "Product 1", "Description 1", new BigDecimal("10.00"));
        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        // Act
        Optional<Product> actualProductOptional = productService.findById(productId);

        // Assert
        assertEquals(Optional.of(expectedProduct), actualProductOptional);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> actualProductOptional = productService.findById(productId);

        // Assert
        assertEquals(Optional.empty(), actualProductOptional);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void save_ShouldSaveProduct() {
        // Arrange
        Product product = new Product(null, "Product 1", "Description 1", new BigDecimal("10.00"));
        Product savedProduct = new Product(1L, "Product 1", "Description 1", new BigDecimal("10.00"));
        when(productRepository.save(product)).thenReturn(savedProduct);

        // Act
        Product actualSavedProduct = productService.save(product);

        // Assert
        assertEquals(savedProduct, actualSavedProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testValidateProducts_AllExistingProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", null),
                new Product(2L, "Product 2", "Description 2", null)
        );
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        when(productRepository.findById(2L)).thenReturn(Optional.of(new Product()));

        // Act
        boolean result = productService.validateProducts(products);

        // Assert
        assertTrue(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
    }

    @Test
    void testValidateProducts_NotAllExistingProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", null),
                new Product(2L, "Product 2", "Description 2", null)
        );
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        boolean result = productService.validateProducts(products);

        // Assert
        assertFalse(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(2L);
    }
}


