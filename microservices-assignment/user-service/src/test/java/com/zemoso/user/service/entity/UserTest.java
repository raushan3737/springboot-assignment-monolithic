package com.zemoso.user.service.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserEntityWithAllPropertiesSet() {
        // Create a new user with all properties set
        User user = new User();
        user.setId(1L);
        user.setUsername("raushan");
        user.setEmail("raushan@example.com");
        user.setPassword("password123");

        // Verify the user properties
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("raushan", user.getUsername());
        Assertions.assertEquals("raushan@example.com", user.getEmail());
        Assertions.assertEquals("password123", user.getPassword());
    }

    @Test
    public void testUserEntityWithMissingProperties() {
        // Create a new user without setting any properties
        User user = new User();

        // Verify that the properties are null or have default values
        Assertions.assertNull(user.getId());
        Assertions.assertNull(user.getUsername());
        Assertions.assertNull(user.getEmail());
        Assertions.assertNull(user.getPassword());
    }

    @Test
    public void testUserEntityWithPartialPropertiesSet() {
        // Create a new user with some properties set
        User user = new User();
        user.setUsername("raushan");
        user.setPassword("password123");

        // Verify the user properties
        Assertions.assertNull(user.getId());
        Assertions.assertEquals("raushan", user.getUsername());
        Assertions.assertNull(user.getEmail());
        Assertions.assertEquals("password123", user.getPassword());
    }

    @Test
    public void testUserEntityPropertyModification() {
        // Create a new user and modify its properties
        User user = new User();
        user.setId(1L);
        user.setUsername("raushan");
        user.setEmail("raushan@example.com");
        user.setPassword("password123");

        // Modify the properties
        user.setUsername("raushanzee");
        user.setEmail("raushanzee@example.com");

        // Verify the modified properties
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("raushanzee", user.getUsername());
        Assertions.assertEquals("raushanzee@example.com", user.getEmail());
        Assertions.assertEquals("password123", user.getPassword());
    }
}
