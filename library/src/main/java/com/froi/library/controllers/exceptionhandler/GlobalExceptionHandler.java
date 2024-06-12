package com.froi.library.controllers.exceptionhandler;

import com.froi.library.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<String> handleDuplcatedEntityException(DuplicatedEntityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(EntitySyntaxException.class)
    public ResponseEntity<String> handleEntitySyntaxException(EntitySyntaxException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(DenegatedActionException.class)
    public ResponseEntity<String> handleDenegatedActionException(DenegatedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    
    @ExceptionHandler(UploadDataFileException.class)
    public ResponseEntity<String> handleUploadDataFileException(UploadDataFileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
