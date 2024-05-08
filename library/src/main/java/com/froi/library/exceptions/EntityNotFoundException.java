package com.froi.library.exceptions;

public class EntityNotFoundException extends ServiceException{
    public EntityNotFoundException() {
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}
