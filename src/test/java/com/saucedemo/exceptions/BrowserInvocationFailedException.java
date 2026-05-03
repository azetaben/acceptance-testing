package com.saucedemo.exceptions;

import java.io.Serial;

public class BrowserInvocationFailedException extends FrameworkException {

    @Serial
    private static final long serialVersionUID = 1L;

    public BrowserInvocationFailedException(String message) {
        super(message);
    }

    public BrowserInvocationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
