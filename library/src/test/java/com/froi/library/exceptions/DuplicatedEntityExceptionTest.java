package com.froi.library.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DuplicatedEntityExceptionTest {
    
    public static final String EXCEPTION_MESSAGE = "Test message";
    
    @Test
    void testDuplicatedEntityException() {
        // Arrange
        String message = EXCEPTION_MESSAGE;
        
        // Act
        DuplicatedEntityException exception = new DuplicatedEntityException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    void testDuplicatedEntityExceptionNoArgs() {
        // Arrange and Act
        DuplicatedEntityException exception = new DuplicatedEntityException();
        
        // Assert
        assertNotNull(exception);
    }
    
}
