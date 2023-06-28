package com.zemoso.easycart.service.impl;

import com.zemoso.easycart.dto.ProductDTO;
import com.zemoso.easycart.entity.Product;
import com.zemoso.easycart.repository.ProductRepository;
import com.zemoso.easycart.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO convertProductToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public Product convertProductToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long theId) {

        return productRepository.findById(theId);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public boolean validateProducts(List<Product> products) {
        for (Product product : products) {
            Long productId = product.getId();
            if (!productRepository.findById(productId).isPresent()) {
                return false;
            }
        }
        return true;
    }

}
