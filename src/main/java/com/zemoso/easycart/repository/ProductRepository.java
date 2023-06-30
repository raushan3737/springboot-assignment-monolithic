package com.zemoso.easycart.repository;

import com.zemoso.easycart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Long> {
}
