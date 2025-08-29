package com.mukul.user.mgm.application.service;

import com.mukul.user.mgm.application.exception.UserException;
import com.mukul.user.mgm.application.model.User;
import com.mukul.user.mgm.application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User testUser;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);

        testUser = new User();
        testUser.setUserName("testUser");
        testUser.setPassWord("password123");
        testUser.setRole("USER");

        updatedUser = new User();
        updatedUser.setUserName("testUser");
        updatedUser.setPassWord("newPassword456");
        updatedUser.setRole("ADMIN");
    }

    @Test
    void getAllUsers_WhenUsersExist_ShouldReturnUserList() throws UserException {
        // Given
        List<User> expectedUsers = Arrays.asList(testUser, updatedUser);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> actualUsers = userService.getAllUsers();

        // Then
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsersExist_ShouldThrowUserException() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getAllUsers();
        });
        assertEquals("No users found", exception.getMessage());
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_WhenRepositoryReturnsNull_ShouldThrowUserException() {
        // Given
        when(userRepository.findAll()).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getAllUsers();
        });
        assertEquals("No users found", exception.getMessage());
        verify(userRepository).findAll();
    }

    @Test
    void getUserByUserName_WhenUserExists_ShouldReturnUser() throws UserException {
        // Given
        String userName = "testUser";
        when(userRepository.findByUserName(userName)).thenReturn(testUser);

        // When
        User actualUser = userService.getUserByUserName(userName);

        // Then
        assertNotNull(actualUser);
        assertEquals(testUser, actualUser);
        assertEquals(userName, actualUser.getUserName());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void getUserByUserName_WhenUserDoesNotExist_ShouldThrowUserException() {
        // Given
        String userName = "nonExistentUser";
        when(userRepository.findByUserName(userName)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserByUserName(userName);
        });
        assertEquals("User not found with userName: " + userName, exception.getMessage());
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void getUserByUserName_WithNullUserName_ShouldThrowUserException() {
        // Given
        when(userRepository.findByUserName(null)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserByUserName(null);
        });
        assertEquals("User not found with userName: null", exception.getMessage());
        verify(userRepository).findByUserName(null);
    }

    @Test
    void getUserByUserName_WithEmptyUserName_ShouldThrowUserException() {
        // Given
        String emptyUserName = "";
        when(userRepository.findByUserName(emptyUserName)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserByUserName(emptyUserName);
        });
        assertEquals("User not found with userName: ", exception.getMessage());
        verify(userRepository).findByUserName(emptyUserName);
    }

    @Test
    void createUser_WithValidUser_ShouldReturnSavedUser() {
        // Given
        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        User savedUser = userService.createUser(testUser);

        // Then
        assertNotNull(savedUser);
        assertEquals(testUser, savedUser);
        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_WithNullUser_ShouldCallRepositorySave() {
        // Given
        User nullUser = null;
        when(userRepository.save(nullUser)).thenReturn(nullUser);

        // When
        User result = userService.createUser(nullUser);

        // Then
        assertNull(result);
        verify(userRepository).save(nullUser);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() throws UserException {
        // Given
        String userName = "testUser";
        User existingUser = new User();
        existingUser.setUserName(userName);
        existingUser.setPassWord("oldPassword");
        existingUser.setRole("USER");

        when(userRepository.findByUserName(userName)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        User result = userService.updateUser(userName, updatedUser);

        // Then
        assertNotNull(result);
        assertEquals(updatedUser.getPassWord(), existingUser.getPassWord());
        assertEquals(updatedUser.getRole(), existingUser.getRole());
        assertEquals(userName, existingUser.getUserName()); // userName should remain unchanged
        verify(userRepository).findByUserName(userName);
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowUserException() {
        // Given
        String userName = "nonExistentUser";
        when(userRepository.findByUserName(userName)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.updateUser(userName, updatedUser);
        });
        assertEquals("User not found with userName: " + userName, exception.getMessage());
        verify(userRepository).findByUserName(userName);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WithNullUserName_ShouldThrowUserException() {
        // Given
        when(userRepository.findByUserName(null)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.updateUser(null, updatedUser);
        });
        assertEquals("User not found with userName: null", exception.getMessage());
        verify(userRepository).findByUserName(null);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WithNullUpdatedUser_ShouldThrowNullPointerException() {
        // Given
        String userName = "testUser";
        when(userRepository.findByUserName(userName)).thenReturn(testUser);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            userService.updateUser(userName, null);
        });
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() throws UserException {
        // Given
        String userName = "testUser";
        when(userRepository.findByUserName(userName)).thenReturn(testUser);

        // When
        userService.deleteUser(userName);

        // Then
        verify(userRepository).findByUserName(userName);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowUserException() {
        // Given
        String userName = "nonExistentUser";
        when(userRepository.findByUserName(userName)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteUser(userName);
        });
        assertEquals("User not found with userName: " + userName, exception.getMessage());
        verify(userRepository).findByUserName(userName);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deleteUser_WithNullUserName_ShouldThrowUserException() {
        // Given
        when(userRepository.findByUserName(null)).thenReturn(null);

        // When & Then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteUser(null);
        });
        assertEquals("User not found with userName: null", exception.getMessage());
        verify(userRepository).findByUserName(null);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void userExists_WhenUserExists_ShouldReturnTrue() {
        // Given
        String userName = "testUser";
        when(userRepository.findByUserName(userName)).thenReturn(testUser);

        // When
        boolean exists = userService.userExists(userName);

        // Then
        assertTrue(exists);
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void userExists_WhenUserDoesNotExist_ShouldReturnFalse() {
        // Given
        String userName = "nonExistentUser";
        when(userRepository.findByUserName(userName)).thenReturn(null);

        // When
        boolean exists = userService.userExists(userName);

        // Then
        assertFalse(exists);
        verify(userRepository).findByUserName(userName);
    }

    @Test
    void userExists_WithNullUserName_ShouldReturnFalse() {
        // Given
        when(userRepository.findByUserName(null)).thenReturn(null);

        // When
        boolean exists = userService.userExists(null);

        // Then
        assertFalse(exists);
        verify(userRepository).findByUserName(null);
    }

    @Test
    void userExists_WithEmptyUserName_ShouldReturnFalse() {
        // Given
        String emptyUserName = "";
        when(userRepository.findByUserName(emptyUserName)).thenReturn(null);

        // When
        boolean exists = userService.userExists(emptyUserName);

        // Then
        assertFalse(exists);
        verify(userRepository).findByUserName(emptyUserName);
    }
}
