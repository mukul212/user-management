package com.mukul.user.mgm.tests.controller;

import com.mukul.user.mgm.application.controller.UserController;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void findByRole_WhenUsersExist_ReturnsUsers() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User("user1", "pass1", "admin")
        );
        Mockito.when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = userController.findByRole("admin");

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());
    }

    @Test
    public void findByRole_WhenNoUsers_ReturnsNotFound() {
        // Arrange
        Mockito.when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<User>> response = userController.findByRole("admin");

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByUserName_WhenUserExists_ReturnsUser() {
        // Arrange
        User mockUser = new User("testUser", "pass", "role");
        Mockito.when(userRepository.findByUserName("testUser")).thenReturn(mockUser);

        // Act
        ResponseEntity<User> response = userController.findByUserName("testUser");

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("testUser", response.getBody().getUserName());
    }

    @Test
    public void createUser_WhenUserDoesNotExist_CreatesUser() {
        // Arrange
        User newUser = new User("newUser", "pass", "role");
        Mockito.when(userRepository.exists("newUser")).thenReturn(false);
        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();

        // Act
        ResponseEntity<Void> response = userController.createUser(newUser, ucBuilder);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Mockito.verify(userRepository, Mockito.times(1)).save(newUser);
    }

    @Test
    public void getAllUsers_WhenUsersExist_ReturnsUsers() throws Exception {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                new User("user1", "pass1", "role1")
        );
        Mockito.when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());
    }
}