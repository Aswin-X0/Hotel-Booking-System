package com.example.VIBES.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTests {
    
    @Test
    void testAllArgsConstructorAndGetters() {
        User user = new User(1L,"John",25,"9876543210","john@example.com","john123","secret","ADMIN"
        );

        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals(25, user.getAge());
        assertEquals("9876543210", user.getNumber());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("john123", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        user.setId(2L);
        user.setName("Alice");
        user.setAge(22);
        user.setNumber("9999999999");
        user.setEmail("alice@example.com");
        user.setUsername("alice123");
        user.setPassword("pass123");
        user.setRole("USER");

        assertEquals(2L, user.getId());
        assertEquals("Alice", user.getName());
        assertEquals(22, user.getAge());
        assertEquals("9999999999", user.getNumber());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("alice123", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testToStringDoesNotIncludePassword() {
        User user = new User(3L,"Bob", 30,"8888888888","bob@example.com","bob123","hiddenPassword","ADMIN"
        );

        String result = user.toString();

        assertNotNull(result);
        assertTrue(result.contains("Bob"));
        assertFalse(result.contains("hiddenPassword"));
    }

    @Test
    void testHashCodeConsistency() {
        User user1 = new User(
                1L,
                "John",
                25,
                "9876543210",
                "john@example.com",
                "john123",
                "secret",
                "ADMIN"
        );

        User user2 = new User(
                1L,
                "John",
                25,
                "9876543210",
                "john@example.com",
                "john123",
                "secret",
                "ADMIN"
        );

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}