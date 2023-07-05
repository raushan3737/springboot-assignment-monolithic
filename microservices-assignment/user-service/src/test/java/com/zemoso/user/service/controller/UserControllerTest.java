package com.zemoso.user.service.controller;


import com.zemoso.user.service.dto.UserDTO;
import com.zemoso.user.service.entity.User;
import com.zemoso.user.service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();
        userController = new UserController(userService);
    }

    @Test
    void getAllUsers_ReturnsAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users.size(), response.getBody().size());
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() {
        Long userId = 1L;
        User user = new User();
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserById_NonExistingId_ReturnsNotFound() {
        Long userId = 1L;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addUser_ReturnsCreatedUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userService.convertToEntity(userDTO)).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        UserDTO expectedUserDTO = modelMapper.map(user, UserDTO.class);

        ResponseEntity<UserDTO> response = userController.addUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateUser_ReturnsUpdatedUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userService.convertToEntity(userDTO)).thenReturn(user);
        when(userService.save(user)).thenReturn(user);
        UserDTO expectedUserDTO = modelMapper.map(user, UserDTO.class);

        ResponseEntity<UserDTO> response = userController.updateUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
