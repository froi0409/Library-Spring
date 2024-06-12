package com.froi.library.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceExceptionTest {
    
    public static final String EXCEPTION_MESSAGE = "Test message";
    
    @Test
    void testServiceException() {
        // Arrange
        String message = EXCEPTION_MESSAGE;
        
        // Act
        ServiceException exception = new ServiceException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    void testServiceExceptionNoArgs() {
        // Arrange and Act
        ServiceException exception = new ServiceException();
        
        // Assert
        assertNotNull(exception);
    }
    
}
