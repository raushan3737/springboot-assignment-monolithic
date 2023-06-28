package com.zemoso.easycart.controller;

import com.zemoso.easycart.dto.UserDTO;
import com.zemoso.easycart.entity.User;
import com.zemoso.easycart.exception.CustomException;
import com.zemoso.easycart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(userDTO -> userService.convertToDTO(userDTO))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);

    }


    @PostMapping("/")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.convertToEntity(userDTO);
            user.setId(0L);
            User savedUser = userService.save(user);
            UserDTO savedUserDTO = userService.convertToDTO(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
        } catch (Exception e) {
            throw new CustomException("Failed to add user: " + e.getMessage());
        }
    }


    @PutMapping("/")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.convertToEntity(userDTO);
            User updatedUser = userService.save(user);
            UserDTO updatedUserDTO = userService.convertToDTO(updatedUser);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (Exception e) {
            // Handle the exception and return an appropriate response
            throw new CustomException("Failed to add user: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = userService.convertToDTO(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}