package com.mukul.user.mgm.application.repository;

import com.mukul.user.mgm.application.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User adminUser;
    private User managerUser;
    private User userRole;

    @BeforeEach
    public void setUp() {
        adminUser = new User("admin1", "password123", "admin");
        managerUser = new User("manager1", "password456", "manager");
        userRole = new User("user1", "password789", "user");

        entityManager.persistAndFlush(adminUser);
        entityManager.persistAndFlush(managerUser);
        entityManager.persistAndFlush(userRole);
    }

    // findByRoleIgnoreCase Tests
    @Test
    public void testFindByRoleIgnoreCase_ExactMatch() {
        List<User> result = userRepository.findByRoleIgnoreCase("admin");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin1", result.get(0).getUserName());
        assertEquals("admin", result.get(0).getRole());
    }

    @Test
    public void testFindByRoleIgnoreCase_IgnoreCase_Uppercase() {
        List<User> result = userRepository.findByRoleIgnoreCase("ADMIN");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin1", result.get(0).getUserName());
    }

    @Test
    public void testFindByRoleIgnoreCase_IgnoreCase_MixedCase() {
        List<User> result = userRepository.findByRoleIgnoreCase("AdMiN");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin1", result.get(0).getUserName());
    }

    @Test
    public void testFindByRoleIgnoreCase_IgnoreCase_Lowercase() {
        List<User> result = userRepository.findByRoleIgnoreCase("manager");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("manager1", result.get(0).getUserName());
    }

    @Test
    public void testFindByRoleIgnoreCase_MultipleUsersWithSameRole() {
        User anotherAdmin = new User("admin2", "password999", "admin");
        entityManager.persistAndFlush(anotherAdmin);

        List<User> result = userRepository.findByRoleIgnoreCase("admin");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> "admin1".equals(u.getUserName())));
        assertTrue(result.stream().anyMatch(u -> "admin2".equals(u.getUserName())));
    }

    @Test
    public void testFindByRoleIgnoreCase_NonExistentRole() {
        List<User> result = userRepository.findByRoleIgnoreCase("nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByRoleIgnoreCase_EmptyString() {
        List<User> result = userRepository.findByRoleIgnoreCase("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByRoleIgnoreCase_WhitespaceString() {
        List<User> result = userRepository.findByRoleIgnoreCase("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByRoleIgnoreCase_NullRole() {
        List<User> result = userRepository.findByRoleIgnoreCase(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // findByUserName Tests
    @Test
    public void testFindByUserName_ExistingUser() {
        User result = userRepository.findByUserName("admin1");

        assertNotNull(result);
        assertEquals("admin1", result.getUserName());
        assertEquals("password123", result.getPassWord());
        assertEquals("admin", result.getRole());
    }

    @Test
    public void testFindByUserName_AnotherExistingUser() {
        User result = userRepository.findByUserName("manager1");

        assertNotNull(result);
        assertEquals("manager1", result.getUserName());
        assertEquals("password456", result.getPassWord());
        assertEquals("manager", result.getRole());
    }

    @Test
    public void testFindByUserName_NonExistentUser() {
        User result = userRepository.findByUserName("nonexistent");

        assertNull(result);
    }

    @Test
    public void testFindByUserName_EmptyString() {
        User result = userRepository.findByUserName("");

        assertNull(result);
    }

    @Test
    public void testFindByUserName_WhitespaceString() {
        User result = userRepository.findByUserName("   ");

        assertNull(result);
    }

    @Test
    public void testFindByUserName_NullUserName() {
        User result = userRepository.findByUserName(null);

        assertNull(result);
    }

    @Test
    public void testFindByUserName_CaseSensitive() {
        User result = userRepository.findByUserName("ADMIN1");

        assertNull(result); // Should be case sensitive
    }

    // Inherited JpaRepository method tests
//    @Test
//    public void testFindById_ExistingUser() {
//        Optional<User> result = userRepository.findById("admin1");
//
//        assertTrue(result.isPresent());
//        assertEquals("admin1", result.get().getUserName());
//        assertEquals("admin", result.get().getRole());
//    }
//
//    @Test
//    public void testFindById_NonExistentUser() {
//        Optional<User> result = userRepository.findById("nonexistent");
//
//        assertFalse(result.isPresent());
//    }

    @Test
    public void testFindAll() {
        List<User> result = userRepository.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(u -> "admin1".equals(u.getUserName())));
        assertTrue(result.stream().anyMatch(u -> "manager1".equals(u.getUserName())));
        assertTrue(result.stream().anyMatch(u -> "user1".equals(u.getUserName())));
    }

    @Test
    public void testSave_NewUser() {
        User newUser = new User("newuser", "newpassword", "guest");

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser);
        assertEquals("newuser", savedUser.getUserName());
        assertEquals("newpassword", savedUser.getPassWord());
        assertEquals("guest", savedUser.getRole());

        // Verify it's persisted
        User foundUser = userRepository.findByUserName("newuser");
        assertNotNull(foundUser);
        assertEquals("guest", foundUser.getRole());
    }

    @Test
    public void testSave_UpdateExistingUser() {
        User existingUser = userRepository.findByUserName("admin1");
        existingUser.setRole("superadmin");

        User updatedUser = userRepository.save(existingUser);

        assertNotNull(updatedUser);
        assertEquals("admin1", updatedUser.getUserName());
        assertEquals("superadmin", updatedUser.getRole());

        // Verify update persisted
        User foundUser = userRepository.findByUserName("admin1");
        assertEquals("superadmin", foundUser.getRole());
    }

//    @Test
//    public void testDeleteById() {
//        assertTrue(userRepository.existsById("admin1"));
//
//        userRepository.deleteById("admin1");
//
//        assertFalse(userRepository.existsById("admin1"));
//        assertNull(userRepository.findByUserName("admin1"));
//    }

    @Test
    public void testCount() {
        long count = userRepository.count();

        assertEquals(3, count);
    }

//    @Test
//    public void testExistsById_ExistingUser() {
//        boolean exists = userRepository.existsById("admin1");
//
//        assertTrue(exists);
//    }
//
//    @Test
//    public void testExistsById_NonExistentUser() {
//        boolean exists = userRepository.existsById("nonexistent");
//
//        assertFalse(exists);
//    }

    // Edge cases and boundary conditions
    @Test
    public void testFindByRoleIgnoreCase_SpecialCharacters() {
        User specialUser = new User("special", "pass", "admin-user");
        entityManager.persistAndFlush(specialUser);

        List<User> result = userRepository.findByRoleIgnoreCase("admin-user");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("special", result.get(0).getUserName());
    }

    @Test
    public void testFindByUserName_SpecialCharacters() {
        User specialUser = new User("user@domain.com", "pass", "user");
        entityManager.persistAndFlush(specialUser);

        User result = userRepository.findByUserName("user@domain.com");

        assertNotNull(result);
        assertEquals("user@domain.com", result.getUserName());
    }

    @Test
    public void testFindByRoleIgnoreCase_NumericRole() {
        User numericUser = new User("numeric", "pass", "123");
        entityManager.persistAndFlush(numericUser);

        List<User> result = userRepository.findByRoleIgnoreCase("123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("numeric", result.get(0).getUserName());
    }

    @Test
    public void testRepository_EmptyDatabase() {
        userRepository.deleteAll();

        List<User> allUsers = userRepository.findAll();
        List<User> adminUsers = userRepository.findByRoleIgnoreCase("admin");
        User foundUser = userRepository.findByUserName("admin1");

        assertTrue(allUsers.isEmpty());
        assertTrue(adminUsers.isEmpty());
        assertNull(foundUser);
        assertEquals(0, userRepository.count());
    }
}
