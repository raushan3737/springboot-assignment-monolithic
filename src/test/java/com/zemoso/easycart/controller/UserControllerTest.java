package com.zemoso.easycart.controller;


import com.zemoso.easycart.dto.UserDTO;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.exception.CustomException;
import com.zemoso.easycart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllUsers_ReturnsListOfUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "user1", "user1@example.com", "password1"));
        userList.add(new User(2L, "user2", "user2@example.com", "password2"));
        Mockito.when(userService.findAll()).thenReturn(userList);

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList.size(), response.getBody().size());
    }

    @Test
    void testAddUser_ValidUserDTO_ReturnsCreated() {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        User user = new User(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToEntity(userDTO)).thenReturn(user);
        Mockito.when(userService.save(user)).thenReturn(user);
        UserDTO expectedUserDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToDTO(user)).thenReturn(expectedUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.addUser(userDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedUserDTO, response.getBody());
    }

    @Test
    void testAddUser_ExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToEntity(userDTO)).thenThrow(new CustomException("Failed to add user"));

        // Act and Assert
        assertThrows(CustomException.class, () -> userController.addUser(userDTO));
    }


    @Test
    void testUpdateUser_ValidUserDTO_ReturnsUpdatedUser() {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        User user = new User(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToEntity(userDTO)).thenReturn(user);
        Mockito.when(userService.save(user)).thenReturn(user);
        UserDTO expectedUserDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToDTO(user)).thenReturn(expectedUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.updateUser(userDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserDTO, response.getBody());
    }

    @Test
    void testUpdateUser_ExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToEntity(userDTO)).thenThrow(new CustomException("Failed to update user::"));

        // Act and Assert
        assertThrows(CustomException.class, () -> userController.updateUser(userDTO));
    }


    @Test
    void testGetUserById_ExistingUserId_ReturnsUserDTO() {
        // Arrange
        Long userId = 1L;
        User user = new User(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.findById(userId)).thenReturn(Optional.of(user));
        UserDTO expectedUserDTO = new UserDTO(1L, "user1", "user1@example.com", "password1");
        Mockito.when(userService.convertToDTO(user)).thenReturn(expectedUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserDTO, response.getBody());
    }

    @Test
    void testGetUserById_NonExistingUserId_ReturnsNotFound() {
        // Arrange
        Long userId = 1L;
        Mockito.when(userService.findById(userId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
