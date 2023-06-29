package com.zemoso.easycart.repository;

import com.zemoso.easycart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
