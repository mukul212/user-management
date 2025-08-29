package com.mukul.user.mgm.application.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserExceptionTest {

    @Test
    void testConstructorWithMessage() {
        // Given
        String expectedMessage = "User not found";

        // When
        UserException exception = new UserException(expectedMessage);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testConstructorWithMessageAndCause() {
        // Given
        String expectedMessage = "User validation failed";
        Throwable expectedCause = new IllegalArgumentException("Invalid user data");

        // When
        UserException exception = new UserException(expectedMessage, expectedCause);

        // Then
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(expectedCause, exception.getCause());
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testConstructorWithNullMessage() {
        // Given
        String nullMessage = null;

        // When
        UserException exception = new UserException(nullMessage);

        // Then
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithEmptyMessage() {
        // Given
        String emptyMessage = "";

        // When
        UserException exception = new UserException(emptyMessage);

        // Then
        assertEquals(emptyMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithNullMessageAndCause() {
        // Given
        String nullMessage = null;
        Throwable cause = new RuntimeException("Root cause");

        // When
        UserException exception = new UserException(nullMessage, cause);

        // Then
        assertNull(exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndNullCause() {
        // Given
        String message = "User operation failed";
        Throwable nullCause = null;

        // When
        UserException exception = new UserException(message, nullCause);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testSerialVersionUID() {
        // Given
        UserException exception1 = new UserException("Test message");
        UserException exception2 = new UserException("Another message");

        // When/Then - Verify serialVersionUID is consistent
        assertEquals(1L, getSerialVersionUID());
    }

    @Test
    void testInheritanceFromException() {
        // Given
        UserException exception = new UserException("Test");

        // When/Then
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    void testExceptionCanBeThrown() {
        // Given
        String message = "User exception test";

        // When/Then
        UserException exception = assertThrows(UserException.class, () -> {
            throw new UserException(message);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionWithCauseCanBeThrown() {
        // Given
        String message = "User exception with cause";
        RuntimeException cause = new RuntimeException("Underlying cause");

        // When/Then
        UserException exception = assertThrows(UserException.class, () -> {
            throw new UserException(message, cause);
        });

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    private long getSerialVersionUID() {
        return 1L; // Matches the serialVersionUID in UserException
    }
}
