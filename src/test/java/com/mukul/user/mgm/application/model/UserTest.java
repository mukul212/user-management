package com.mukul.user.mgm.application.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    // Constructor Tests
    @Test
    public void testNoArgsConstructor() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getUserName());
        assertNull(user.getPassWord());
        assertNull(user.getRole());
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User("john_doe", "password123", "admin");

        assertNotNull(user);
        assertEquals("john_doe", user.getUserName());
        assertEquals("password123", user.getPassWord());
        assertEquals("admin", user.getRole());
    }

    @Test
    public void testAllArgsConstructorWithNullValues() {
        User user = new User(null, null, null);

        assertNotNull(user);
        assertNull(user.getUserName());
        assertNull(user.getPassWord());
        assertNull(user.getRole());
    }

    @Test
    public void testAllArgsConstructorWithEmptyStrings() {
        User user = new User("", "", "");

        assertNotNull(user);
        assertEquals("", user.getUserName());
        assertEquals("", user.getPassWord());
        assertEquals("", user.getRole());
    }

    // Getter and Setter Tests
    @Test
    public void testSetAndGetUserName() {
        user.setUserName("testuser");
        assertEquals("testuser", user.getUserName());
    }

    @Test
    public void testSetAndGetUserNameWithNull() {
        user.setUserName(null);
        assertNull(user.getUserName());
    }

    @Test
    public void testSetAndGetUserNameWithEmptyString() {
        user.setUserName("");
        assertEquals("", user.getUserName());
    }

    @Test
    public void testSetAndGetUserNameWithSpecialCharacters() {
        user.setUserName("user@domain.com");
        assertEquals("user@domain.com", user.getUserName());
    }

    @Test
    public void testSetAndGetUserNameWithLongString() {
        String longUserName = "a".repeat(255);
        user.setUserName(longUserName);
        assertEquals(longUserName, user.getUserName());
    }

    @Test
    public void testSetAndGetPassWord() {
        user.setPassWord("password123");
        assertEquals("password123", user.getPassWord());
    }

    @Test
    public void testSetAndGetPassWordWithNull() {
        user.setPassWord(null);
        assertNull(user.getPassWord());
    }

    @Test
    public void testSetAndGetPassWordWithEmptyString() {
        user.setPassWord("");
        assertEquals("", user.getPassWord());
    }

    @Test
    public void testSetAndGetPassWordWithSpecialCharacters() {
        user.setPassWord("p@ssw0rd!#$");
        assertEquals("p@ssw0rd!#$", user.getPassWord());
    }

    @Test
    public void testSetAndGetRole() {
        user.setRole("admin");
        assertEquals("admin", user.getRole());
    }

    @Test
    public void testSetAndGetRoleWithNull() {
        user.setRole(null);
        assertNull(user.getRole());
    }

    @Test
    public void testSetAndGetRoleWithEmptyString() {
        user.setRole("");
        assertEquals("", user.getRole());
    }

    @Test
    public void testSetAndGetRoleWithSpecialCharacters() {
        user.setRole("admin-role");
        assertEquals("admin-role", user.getRole());
    }

    @Test
    public void testSetAndGetRoleWithMixedCase() {
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    // Equals and HashCode Tests (Lombok generated)
    @Test
    public void testEqualsWithSameValues() {
        User user1 = new User("john", "password", "admin");
        User user2 = new User("john", "password", "admin");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentUserNames() {
        User user1 = new User("john", "password", "admin");
        User user2 = new User("jane", "password", "admin");

        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentPasswords() {
        User user1 = new User("john", "password1", "admin");
        User user2 = new User("john", "password2", "admin");

        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithDifferentRoles() {
        User user1 = new User("john", "password", "admin");
        User user2 = new User("john", "password", "user");

        assertNotEquals(user1, user2);
    }

    @Test
    public void testEqualsWithNull() {
        User user1 = new User("john", "password", "admin");

        assertNotEquals(user1, null);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        User user1 = new User("john", "password", "admin");
        String notAUser = "not a user";

        assertNotEquals(user1, notAUser);
    }

    @Test
    public void testEqualsWithSameInstance() {
        User user1 = new User("john", "password", "admin");

        assertEquals(user1, user1);
    }

    @Test
    public void testEqualsWithAllNullValues() {
        User user1 = new User(null, null, null);
        User user2 = new User(null, null, null);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithMixedNullValues() {
        User user1 = new User("john", null, "admin");
        User user2 = new User("john", null, "admin");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsWithOneNullField() {
        User user1 = new User("john", null, "admin");
        User user2 = new User("john", "password", "admin");

        assertNotEquals(user1, user2);
    }

    // ToString Test (Lombok generated)
    @Test
    public void testToString() {
        User user = new User("john", "password", "admin");
        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("john"));
        assertTrue(toString.contains("password"));
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("User"));
    }

    @Test
    public void testToStringWithNullValues() {
        User user = new User(null, null, null);
        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("User"));
    }

    @Test
    public void testToStringWithEmptyValues() {
        User user = new User("", "", "");
        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("User"));
    }

    // Edge Cases and Boundary Tests
    @Test
    public void testUserWithWhitespaceValues() {
        User user = new User("  ", "  ", "  ");

        assertEquals("  ", user.getUserName());
        assertEquals("  ", user.getPassWord());
        assertEquals("  ", user.getRole());
    }

    @Test
    public void testUserWithUnicodeCharacters() {
        User user = new User("用户", "密码", "管理员");

        assertEquals("用户", user.getUserName());
        assertEquals("密码", user.getPassWord());
        assertEquals("管理员", user.getRole());
    }

    @Test
    public void testUserWithNumericStrings() {
        User user = new User("12345", "67890", "999");

        assertEquals("12345", user.getUserName());
        assertEquals("67890", user.getPassWord());
        assertEquals("999", user.getRole());
    }

    @Test
    public void testUserFieldModificationAfterCreation() {
        User user = new User("original", "original", "original");

        user.setUserName("modified");
        user.setPassWord("modified");
        user.setRole("modified");

        assertEquals("modified", user.getUserName());
        assertEquals("modified", user.getPassWord());
        assertEquals("modified", user.getRole());
    }

    @Test
    public void testHashCodeConsistency() {
        User user = new User("john", "password", "admin");
        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeWithNullFields() {
        User user1 = new User(null, "password", null);
        User user2 = new User(null, "password", null);

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
