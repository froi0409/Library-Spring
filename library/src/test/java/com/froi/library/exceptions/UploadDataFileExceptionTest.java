package com.froi.library.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UploadDataFileExceptionTest {
    public static final String EXCEPTION_MESSAGE = "Test message";
    
    @Test
    void testUploadDataFileException() {
        // Arrange
        String message = EXCEPTION_MESSAGE;
        
        // Act
        UploadDataFileException exception = new UploadDataFileException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
    
    @Test
    void testUploadDataFileExceptionNoArgs() {
        // Arrange and Act
        UploadDataFileException exception = new UploadDataFileException();
        
        // Assert
        assertNotNull(exception);
    }
}
