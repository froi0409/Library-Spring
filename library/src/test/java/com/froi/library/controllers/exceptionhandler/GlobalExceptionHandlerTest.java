package com.froi.library.controllers.exceptionhandler;

import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler globalExceptionHandler;
    
    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }
    
    @Test
    void testHandleNotFoundException() {
        // Arrange
        EntityNotFoundException ex = mock(EntityNotFoundException.class);
        
        // Act
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleNotFoundException(ex);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    @Test
    void testHandleDuplicatedEntityException() {
        // Arrange
        DuplicatedEntityException ex = mock(DuplicatedEntityException.class);
        
        // Act
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDuplcatedEntityException(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    void testHandleEntitySyntaxException() {
        // Arrange
        EntitySyntaxException ex = mock(EntitySyntaxException.class);
        
        // Act
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleEntitySyntaxException(ex);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
