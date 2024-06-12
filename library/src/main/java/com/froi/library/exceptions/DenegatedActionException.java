package com.froi.library.exceptions;

public class DenegatedActionException extends ServiceException {
    public DenegatedActionException() {
    }
    public DenegatedActionException(String message) {
        super(message);
    }
}
