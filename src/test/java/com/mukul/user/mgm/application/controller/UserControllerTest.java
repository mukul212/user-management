package com.mukul.user.mgm.application.controller;

import com.mukul.user.mgm.application.exception.UserException;
import com.mukul.user.mgm.application.model.User;
import com.mukul.user.mgm.application.repository.UserRepository;
import com.mukul.user.mgm.application.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

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
    public void testFindByRole_MultipleUsersFound() {
        List<User> users = Arrays.asList(
                new User("admin1", "password1", "admin"),
                new User("admin2", "password2", "admin")
        );
        when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.findByRole("admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(users, response.getBody());
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

    @Test
    public void testGetAllUsers_UsersExist() throws Exception {
        List<User> users = Arrays.asList(
                new User("user1", "password1", "admin"),
                new User("user2", "password2", "user")
        );
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetAllUsers_NoUsersFound() throws Exception {
        when(userService.getAllUsers()).thenThrow(new UserException("No users found"));

        try {
            ResponseEntity<List<User>> response = userController.getAllUsers();
        } catch (Exception e) {
            assertEquals("No users found", e.getMessage());
        }
    }

    @Test
    public void testGetAllUsers_EmptyListReturned() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
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
    public void testFindByRole_NullReturnFromRepository() {
        when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(null);

        ResponseEntity<List<User>> response = userController.findByRole("admin");

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

    @Test
    public void testCreateUser_UserWithNullRole() {
        User user = new User("john_doe", "password", null);
        when(userRepository.exists("john_doe")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    public void testCreateUser_RepositoryExistsThrowsException() {
        User user = new User("john_doe", "password", "admin");
        when(userRepository.exists("john_doe")).thenThrow(new RuntimeException("Database error"));

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();

        try {
            ResponseEntity<Void> response = userController.createUser(user, ucBuilder);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }
    }

    @Test
    public void testCreateUser_RepositorySaveThrowsException() {
        User user = new User("john_doe", "password", "admin");
        when(userRepository.exists("john_doe")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Save failed"));

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();

        try {
            ResponseEntity<Void> response = userController.createUser(user, ucBuilder);
        } catch (RuntimeException e) {
            assertEquals("Save failed", e.getMessage());
        }
    }

    @Test
    public void testGetAllUsers_ServiceThrowsGenericException() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Service error"));

        try {
            ResponseEntity<List<User>> response = userController.getAllUsers();
        } catch (RuntimeException e) {
            assertEquals("Service error", e.getMessage());
        }
    }

    // Edge Cases
    @Test
    public void testFindByRole_CaseInsensitiveSearch() {
        List<User> users = Arrays.asList(new User("admin", "password", "ADMIN"));
        when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.findByRole("admin");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("ADMIN", response.getBody().get(0).getRole());
    }

    @Test
    public void testFindByRole_SpecialCharactersInRole() {
        List<User> users = Arrays.asList(new User("user", "password", "admin-role"));
        when(userRepository.findByRoleIgnoreCase("admin-role")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.findByRole("admin-role");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testFindByUserName_SpecialCharactersInUserName() {
        User user = new User("user@domain.com", "password", "admin");
        when(userRepository.findByUserName("user@domain.com")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("user@domain.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user@domain.com", response.getBody().getUserName());
    }

    @Test
    public void testCreateUser_LongUserName() {
        String longUserName = "a".repeat(255);
        User user = new User(longUserName, "password", "admin");
        when(userRepository.exists(longUserName)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateUser_UriBuilderWithBaseUrl() {
        User user = new User("john_doe", "password", "admin");
        when(userRepository.exists("john_doe")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UriComponentsBuilder ucBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");
        ResponseEntity<Void> response = userController.createUser(user, ucBuilder);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("http://localhost:8080/user/john_doe", response.getHeaders().getLocation().toString());
    }

    private User getUser() {
        User user = new User();
        user.setUserName("admin");
        user.setPassWord("admin");
        user.setRole("admin");
        return user;
    }
}
