package com.zemoso.product.service.controller;


import com.zemoso.product.service.dto.ProductDTO;
import com.zemoso.product.service.entity.Product;
import com.zemoso.product.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> productService.convertProductToDTO(product))
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            ProductDTO productDTO = productService.convertProductToDTO(product);
            return ResponseEntity.ok(productDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        Product product = productService.convertProductToEntity(productDTO);
        product.setId(0L);
        Product savedProduct = productService.save(product);
        ProductDTO savedProductDTO = productService.convertProductToDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    @PutMapping("/")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO) {
        Product product = productService.convertProductToEntity(productDTO);
        Product updatedProduct = productService.save(product);
        ProductDTO updatedProductDTO = productService.convertProductToDTO(updatedProduct);
        return ResponseEntity.ok(updatedProductDTO);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductQuantity(@PathVariable Long productId) {
        Optional<Product> updatedProduct = productService.updateProductQuantity(productId);

        if (updatedProduct.isPresent()) {
            ProductDTO updatedProductDTO = productService.convertProductToDTO(updatedProduct.get());
            return ResponseEntity.ok(updatedProductDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
