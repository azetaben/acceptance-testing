package com.saucedemo.exceptions;

import java.io.Serial;

public class CriteriaSetNumberException extends FrameworkException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CriteriaSetNumberException(String message) {
        super(message);
    }

    public CriteriaSetNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
