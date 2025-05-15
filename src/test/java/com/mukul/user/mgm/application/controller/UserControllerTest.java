package com.mukul.user.mgm.application.controller;

import com.mukul.user.mgm.application.model.User;
import com.mukul.user.mgm.application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Happy Path Tests
    @Test
    public void testFindByRole_UserExists() {
        List<User> users = new ArrayList<>();
        users.add(getUser());
        when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.findByRole("admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        assertEquals(users.get(0).getUserName(), response.getBody().get(0).getUserName());
        assertEquals(users.get(0).getRole(), response.getBody().get(0).getRole());
        assertEquals(users.get(0).getPassWord(), response.getBody().get(0).getPassWord());
    }

    @Test
    public void testFindByRole_UserNotFound() {
        when(userRepository.findByRoleIgnoreCase("nonexistent")).thenReturn(new ArrayList<>());

        ResponseEntity<List<User>> response = userController.findByRole("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindByUserName_UserExists() {
        User user = new User("john_doe", "password", "admin");
        when(userRepository.findByUserName("john_doe")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("john_doe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testFindByUserName_UserNotFound() {
        when(userRepository.findByUserName("nonexistent")).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUser_UserCreated() {
        User user = new User("john_doe", "password", "admin");
        when(userRepository.exists("john_doe")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/user/john_doe", response.getHeaders().getLocation().toString());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        User user = new User("john_doe", "password", "admin");
        when(userRepository.exists("john_doe")).thenReturn(true);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    // Error Path Tests
    @Test
    public void testFindByRole_InvalidRole() {
        // Simulate an invalid role input (e.g., null or empty)
        ResponseEntity<List<User>> response = userController.findByRole(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        response = userController.findByRole("");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindByUserName_InvalidUserName() {
        // Simulate an invalid username input (e.g., null or empty)
        ResponseEntity<User> response = userController.findByUserName(null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        response = userController.findByUserName("");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateUser_UserWithNullUserName() {
        User user = new User(null, "password", "admin");
        when(userRepository.exists(anyString())).thenReturn(false);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUser_UserWithNullPassword() {
        User user = new User("john_doe", null, "admin");
        when(userRepository.exists("john_doe")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private User getUser() {
        User user = new User();
        user.setUserName("admin");
        user.setPassWord("admin");
        user.setRole("admin");
        return user;
    }
}
