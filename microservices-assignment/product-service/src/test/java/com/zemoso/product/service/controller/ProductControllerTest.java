package com.zemoso.product.service.controller;


import com.zemoso.product.service.dto.ProductDTO;
import com.zemoso.product.service.entity.Product;
import com.zemoso.product.service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Mocking data
        Product product1 = new Product(1L, "Product 1", "Description 1", BigDecimal.valueOf(10));
        Product product2 = new Product(2L, "Product 2", "Description 2", BigDecimal.valueOf(20));
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.findAll()).thenReturn(products);
        when(productService.convertProductToDTO(product1)).thenReturn(new ProductDTO(1L, "Product 1", "Description 1", BigDecimal.valueOf(10)));
        when(productService.convertProductToDTO(product2)).thenReturn(new ProductDTO(2L, "Product 2", "Description 2", BigDecimal.valueOf(20)));

        // Calling the controller method
        ResponseEntity<List<ProductDTO>> responseEntity = productController.getAllProducts();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ProductDTO> productDTOs = responseEntity.getBody();
        assertEquals(2, productDTOs.size());
        assertEquals("Product 1", productDTOs.get(0).getName());
        assertEquals("Product 2", productDTOs.get(1).getName());

        // Verifying the mock interactions
        verify(productService, times(1)).findAll();
        verify(productService, times(1)).convertProductToDTO(product1);
        verify(productService, times(1)).convertProductToDTO(product2);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void testGetProductById() {
        // Mocking data
        Long productId = 1L;
        Product product = new Product(productId, "Product 1", "Description 1", BigDecimal.valueOf(10));
        when(productService.findById(productId)).thenReturn(Optional.of(product));
        when(productService.convertProductToDTO(product)).thenReturn(new ProductDTO(1L, "Product 1", "Description 1", BigDecimal.valueOf(10)));

        // Calling the controller method
        ResponseEntity<ProductDTO> responseEntity = productController.getProductById(productId);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ProductDTO productDTO = responseEntity.getBody();
        assertEquals("Product 1", productDTO.getName());

        // Verifying the mock interactions
        verify(productService, times(1)).findById(productId);
        verify(productService, times(1)).convertProductToDTO(product);
        verifyNoMoreInteractions(productService);
    }


    @Test
    public void testGetProductById_NotFound() {
        // Mocking data
        Long productId = 1L;
        when(productService.findById(productId)).thenReturn(Optional.empty());

        // Calling the controller method
        ResponseEntity<ProductDTO> responseEntity = productController.getProductById(productId);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Verifying the mock interactions
        verify(productService, times(1)).findById(productId);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void testAddProduct() {
        // Mocking data
        ProductDTO productDTO = new ProductDTO(null, "New Product", "New Description", BigDecimal.valueOf(50));
        Product savedProduct = new Product(1L, "New Product", "New Description", BigDecimal.valueOf(50));
        when(productService.convertProductToEntity(productDTO)).thenReturn(savedProduct);
        when(productService.save(savedProduct)).thenReturn(savedProduct);
        ProductDTO savedProductDTO = new ProductDTO(1L, "New Product", "New Description", BigDecimal.valueOf(50));
        when(productService.convertProductToDTO(savedProduct)).thenReturn(savedProductDTO);

        // Calling the controller method
        ResponseEntity<ProductDTO> responseEntity = productController.addProduct(productDTO);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ProductDTO responseProductDTO = responseEntity.getBody();
        assertEquals(1L, responseProductDTO.getId());

        // Verifying the mock interactions
        verify(productService, times(1)).convertProductToEntity(productDTO);
        verify(productService, times(1)).save(savedProduct);
        verify(productService, times(1)).convertProductToDTO(savedProduct);
        verifyNoMoreInteractions(productService);
    }

    @Test
    void testUpdateProduct() {
        // Mocking data
        ProductDTO productDTO = new ProductDTO(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(75));
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(75));
        when(productService.convertProductToEntity(productDTO)).thenReturn(updatedProduct);
        when(productService.save(updatedProduct)).thenReturn(updatedProduct);
        ProductDTO updatedProductDTO = new ProductDTO(1L, "Updated Product", "Updated Description", BigDecimal.valueOf(75));
        when(productService.convertProductToDTO(updatedProduct)).thenReturn(updatedProductDTO);

        // Calling the controller method
        ResponseEntity<ProductDTO> responseEntity = productController.updateProduct(productDTO);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ProductDTO responseProductDTO = responseEntity.getBody();
        assertEquals("Updated Product", responseProductDTO.getName());

        // Verifying the mock interactions
        verify(productService, times(1)).convertProductToEntity(productDTO);
        verify(productService, times(1)).save(updatedProduct);
        verify(productService, times(1)).convertProductToDTO(updatedProduct);
        verifyNoMoreInteractions(productService);
    }
}
