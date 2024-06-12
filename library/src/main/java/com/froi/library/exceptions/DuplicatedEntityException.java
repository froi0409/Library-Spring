package com.froi.library.exceptions;

public class DuplicatedEntityException extends ServiceException{
    public DuplicatedEntityException() {
    }
    
    public DuplicatedEntityException(String message) {
        super(message);
    }
}
