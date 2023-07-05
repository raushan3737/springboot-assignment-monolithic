package com.zemoso.product.service.service;

import com.zemoso.product.service.dto.ProductDTO;
import com.zemoso.product.service.entity.Product;
import com.zemoso.product.service.repository.ProductRepository;
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

    @Override
    public Optional<Product> updateProductQuantity(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            int quantity = product.getQuantity();
            product.setQuantity(quantity - 1);
            productRepository.save(product);
        }

        return productOptional;
    }


}
