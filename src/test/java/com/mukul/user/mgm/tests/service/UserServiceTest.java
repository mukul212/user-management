package com.mukul.user.mgm.tests.service;

import com.mukul.user.mgm.application.model.User;
import com.mukul.user.mgm.application.repository.UserRepository;
import com.mukul.user.mgm.application.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsers_WhenUsersExist_ReturnsUsersList() throws Exception {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User("user1", "pass1", "role1"),
                new User("user2", "pass2", "role2")
        );
        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getAllUsers_WhenNoUsers_ThrowsException() {
        // Arrange
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> userService.getAllUsers());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }
}