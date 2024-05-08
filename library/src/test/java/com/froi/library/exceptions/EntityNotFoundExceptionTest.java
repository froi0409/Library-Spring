package com.froi.library.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityNotFoundExceptionTest {
    
    public static final String EXCEPTION_MESSAGE = "Test message";
    
    @Test
    void testEntityNotFoundException() {
        // Arrange
        String message = EXCEPTION_MESSAGE;
        
        // Act
        EntityNotFoundException exception = new EntityNotFoundException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    void testEntityNotFoundExceptionNoArgs() {
        // Arrange and Act
        EntityNotFoundException exception = new EntityNotFoundException();
        
        // Assert
        assertNotNull(exception);
    }
    
}
