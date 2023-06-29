package com.zemoso.easycart.service.impl;


import com.zemoso.easycart.dto.UserDTO;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private static final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testConvertToDTO() {
        // Create a User object
        User user = new User(1L, "raushan", "raushan@example.com", "password");

        // Call the convertToDTO method
        UserDTO userDTO = userService.convertToDTO(user);

        // Verify the conversion
        UserDTO expectedDTO = modelMapper.map(user, UserDTO.class);
        Assertions.assertEquals(expectedDTO, userDTO);
    }

    @Test
    void testConvertToEntity() {
        // Create a UserDTO object
        UserDTO userDTO = new UserDTO(1L, "raushan", "raushan@example.com", "password");

        // Call the convertToEntity method
        User user = userService.convertToEntity(userDTO);

        // Verify the conversion
        User expectedUser = modelMapper.map(userDTO, User.class);
        Assertions.assertEquals(expectedUser, user);
    }

    @Test
    void testFindAll() {
        // Create a list of User objects
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "raushan", "raushan@example.com", "password"));
        userList.add(new User(2L, "satwik", "satwik@example.com", "password"));

        // Mock the userRepository's findAll() method
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        // Call the findAll method
        List<User> result = userService.findAll();

        // Verify the result
        Assertions.assertEquals(userList, result);
    }

    @Test
    void testFindById() {
        // Create a User object
        User user = new User(1L, "raushan", "raushan@example.com", "password");

        // Mock the userRepository's findById() method
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the findById method
        Optional<User> result = userService.findById(1L);

        // Verify the result
        Assertions.assertEquals(Optional.of(user), result);
    }

    @Test
    void testSave() {
        // Create a User object
        User user = new User(1L, "raushan", "raushan@example.com", "password");

        // Mock the userRepository's save() method
        Mockito.when(userRepository.save(user)).thenReturn(user);

        // Call the save method
        User result = userService.save(user);

        // Verify the result
        Assertions.assertEquals(user, result);
    }
}
