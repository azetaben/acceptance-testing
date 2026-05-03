package com.saucedemo.exceptions;

public class CriteriaSetNumberException extends FrameworkException {

    private static final long serialVersionUID = 1L;

    public CriteriaSetNumberException(String message) {
        super(message);
    }

    public CriteriaSetNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
