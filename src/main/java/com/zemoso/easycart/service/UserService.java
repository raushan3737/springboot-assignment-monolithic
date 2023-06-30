package com.zemoso.easycart.service;

import com.zemoso.easycart.dto.UserDTO;
import com.zemoso.easycart.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    public   UserDTO convertToDTO(User user);
    public  User convertToEntity(UserDTO userDTO);
    public List<User> findAll();

    public Optional<User> findById(Long theId);

    public User save(User user);

}
