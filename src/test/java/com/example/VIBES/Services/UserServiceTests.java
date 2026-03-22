package com.example.VIBES.Services;

import com.example.VIBES.DTO.Urequest;
import com.example.VIBES.Model.User;
import com.example.VIBES.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("john");

        User user2 = new User();
        user2.setUsername("alice");

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("john", users.get(0).getUsername());
        assertEquals("alice", users.get(1).getUsername());
        verify(userRepo).findAll();
    }

    @Test
    void testGetUserByIdWhenFound() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserbyId(1L);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepo).findById(1L);
    }

    @Test
    void testGetUserByIdWhenNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserbyId(1L);

        assertNull(result);
        verify(userRepo).findById(1L);
    }

    @Test
    void testGetUserByUsername() {
        User user = new User();
        user.setUsername("john");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userService.getUserbyUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepo).findByUsername("john");
    }

    @Test
    void testUpdateUserWhenUserExists() {
        User existingUser = new User();
        existingUser.setUsername("john");
        existingUser.setName("Old Name");
        existingUser.setAge(20);
        existingUser.setEmail("old@example.com");
        existingUser.setNumber("1111111111");

        Urequest urequest = new Urequest();
        urequest.setName("New Name");
        urequest.setAge(25);
        urequest.setEmail("new@example.com");
        urequest.setNumber("9999999999");

        when(userRepo.findByUsername("john")).thenReturn(existingUser);
        when(userRepo.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.Updateuser("john", urequest);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
        assertEquals(25, updatedUser.getAge());
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("9999999999", updatedUser.getNumber());

        verify(userRepo).findByUsername("john");
        verify(userRepo).save(existingUser);
    }

    @Test
    void testUpdateUserWhenUserDoesNotExist() {
        Urequest urequest = new Urequest();
        urequest.setName("New Name");
        urequest.setAge(25);
        urequest.setEmail("new@example.com");
        urequest.setNumber("9999999999");

        when(userRepo.findByUsername("unknown")).thenReturn(null);

        User result = userService.Updateuser("unknown", urequest);

        assertNull(result);
        verify(userRepo).findByUsername("unknown");
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepo).deleteById(1L);

        userService.Deleteuser(1L);

        verify(userRepo).deleteById(1L);
    }

    @Test
    void testValidateLoginSuccess() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("12345");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userService.validateLogin("john", "12345");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepo).findByUsername("john");
    }

    @Test
    void testValidateLoginWrongPassword() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("12345");

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userService.validateLogin("john", "wrongpass");

        assertNull(result);
        verify(userRepo).findByUsername("john");
    }

    @Test
    void testValidateLoginUserNotFound() {
        when(userRepo.findByUsername("unknown")).thenReturn(null);

        User result = userService.validateLogin("unknown", "12345");

        assertNull(result);
        verify(userRepo).findByUsername("unknown");
    }

    @Test
    void testValidateLoginWhenStoredPasswordIsNull() {
        User user = new User();
        user.setUsername("john");
        user.setPassword(null);

        when(userRepo.findByUsername("john")).thenReturn(user);

        User result = userService.validateLogin("john", "12345");

        assertNull(result);
        verify(userRepo).findByUsername("john");
    }

    @Test
    void testLoadUserByUsernameSuccessWithRole() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("12345");
        user.setRole("ADMIN");

        when(userRepo.findByUsername("john")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("12345", userDetails.getPassword());
        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userRepo).findByUsername("john");
    }

    @Test
    void testLoadUserByUsernameSuccessWithDefaultRole() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("12345");
        user.setRole(null);

        when(userRepo.findByUsername("john")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))
        );

        verify(userRepo).findByUsername("john");
    }

    @Test
    void testLoadUserByUsernameWhenUserNotFound() {
        when(userRepo.findByUsername("unknown")).thenReturn(null);

        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("unknown")
        );

        verify(userRepo).findByUsername("unknown");
    }
}