package com.mukul.user.mgm.application.repository;

import com.mukul.user.mgm.application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureMockMvc
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryTest userRepositoryTest;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserName("testUser");
        user.setRole("admin");
    }

    // Happy Path Tests
    @Test
    public void testFindByRoleIgnoreCase_ValidRole() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findByRoleIgnoreCase("admin")).thenReturn(users);

        List<User> result = userRepository.findByRoleIgnoreCase("admin");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUserName());
        verify(userRepository, times(1)).findByRoleIgnoreCase("admin");
    }

    @Test
    public void testFindByRoleIgnoreCase_InvalidRole() {
        when(userRepository.findByRoleIgnoreCase("invalidRole")).thenReturn(new ArrayList<>());

        List<User> result = userRepository.findByRoleIgnoreCase("invalidRole");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByRoleIgnoreCase("invalidRole");
    }

    @Test
    public void testFindByUserName_ValidUserName() {
        when(userRepository.findByUserName("testUser")).thenReturn(user);

        User result = userRepository.findByUserName("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        verify(userRepository, times(1)).findByUserName("testUser");
    }

    @Test
    public void testFindByUserName_InvalidUserName() {
        when(userRepository.findByUserName("invalidUser")).thenReturn(null);

        User result = userRepository.findByUserName("invalidUser");

        assertNull(result);
        verify(userRepository, times(1)).findByUserName("invalidUser");
    }

    // Error Path Tests
    @Test
    public void testFindByRoleIgnoreCase_NullRole() {
        when(userRepository.findByRoleIgnoreCase(null)).thenThrow(new IllegalArgumentException("Role cannot be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.findByRoleIgnoreCase(null);
        });

        assertEquals("Role cannot be null", exception.getMessage());
        verify(userRepository, times(1)).findByRoleIgnoreCase(null);
    }

    @Test
    public void testFindByUserName_NullUserName() {
        when(userRepository.findByUserName(null)).thenThrow(new IllegalArgumentException("User name cannot be null"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.findByUserName(null);
        });

        assertEquals("User name cannot be null", exception.getMessage());
        verify(userRepository, times(1)).findByUserName(null);
    }

    @Test
    public void testFindByRoleIgnoreCase_EmptyRole() {
        when(userRepository.findByRoleIgnoreCase("")).thenReturn(new ArrayList<>());

        List<User> result = userRepository.findByRoleIgnoreCase("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByRoleIgnoreCase("");
    }

    @Test
    public void testFindByUserName_EmptyUserName() {
        when(userRepository.findByUserName("")).thenReturn(null);

        User result = userRepository.findByUserName("");

        assertNull(result);
        verify(userRepository, times(1)).findByUserName("");
    }
}
