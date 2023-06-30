package com.zemoso.easycart.service.impl;

import com.zemoso.easycart.dto.UserDTO;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.repository.UserRepository;
import com.zemoso.easycart.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }


    // Delegating methods to the repository

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long theId) {
        return userRepository.findById(theId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


}
