package com.saucedemo.exceptions;

import java.io.Serial;

public class FrameworkException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Pass the message that needs to be appended to the stacktrace
     *
     * @param message Details about the exception or custom message
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * @param message Details about the exception or custom message
     * @param cause   Pass the enriched stacktrace or customised stacktrace
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
