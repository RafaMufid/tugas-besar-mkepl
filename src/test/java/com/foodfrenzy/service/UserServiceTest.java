package com.foodfrenzy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserServices;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServices userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setU_id(1);
        user1.setUname("John Doe");
        user1.setUemail("john@example.com");
        user1.setUpassword("password123");

        user2 = new User();
        user2.setU_id(2);
        user2.setUname("Raihan");
        user2.setUemail("raihan123");
        user2.setUpassword("12345");
    }

    @Test
    void testGetAllUser() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUser();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getUname());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        User found = userService.getUser(1);

        assertNotNull(found);
        assertEquals(1, found.getU_id());
        assertEquals("John Doe", found.getUname());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findUserByUemail("john@example.com")).thenReturn(user1);

        User found = userService.getUserByEmail("john@example.com");

        assertNotNull(found);
        assertEquals("john@example.com", found.getUemail());
        verify(userRepository, times(1)).findUserByUemail("john@example.com");
    }

    @Test
    void testUpdateUser() {
        userService.updateUser(user1, 1);
        verify(userRepository, times(1)).save(user1);
        assertEquals(1, user1.getU_id());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testAddUser() {
        userService.addUser(user1);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testValidateLoginCredentials_Success() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

        boolean isValid = userService.validateLoginCredentials("john@example.com", "password123");

        assertTrue(isValid);
    }

    @Test
    void testValidateLoginCredentials_Failure() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

        boolean isValid = userService.validateLoginCredentials("john@example.com", "wrongpassword");

        assertFalse(isValid);
    }
}
