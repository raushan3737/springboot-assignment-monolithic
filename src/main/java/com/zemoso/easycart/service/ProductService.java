package com.zemoso.easycart.service;

import com.zemoso.easycart.dto.ProductDTO;
import com.zemoso.easycart.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    public ProductDTO convertProductToDTO(Product product);

    public Product convertProductToEntity(ProductDTO productDTO);

    public List<Product> findAll();

    public Optional<Product> findById(Long theId);

    public Product save(Product product);

    public boolean validateProducts(List<Product> products);

}
