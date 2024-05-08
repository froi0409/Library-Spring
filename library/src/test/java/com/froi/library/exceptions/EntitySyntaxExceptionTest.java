package com.froi.library.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntitySyntaxExceptionTest {
    
    public static final String EXCEPTION_MESSAGE = "Test message";
    
    @Test
    void testEntitySyntaxException() {
        // Arrange
        String message = EXCEPTION_MESSAGE;
        
        // Act
        EntitySyntaxException exception = new EntitySyntaxException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    void testEntitySyntaxExceptionNoArgs() {
        // Arrange and ACt
        EntitySyntaxException exception = new EntitySyntaxException();
        
        // Assert
        assertNotNull(exception);
    }
    
}
