package com.saucedemo.exceptions;

public class InvalidPathForFilesException extends FrameworkException {

    private static final long serialVersionUID = 1L;

    /**
     * Pass the message that needs to be appended to the stacktrace
     *
     * @param message Details about the exception or custom message
     */
    public InvalidPathForFilesException(String message) {
        super(message);

    }

    /**
     * @param message Details about the exception or custom message
     * @param cause   Pass the enriched stacktrace or customised stacktrace
     */
    public InvalidPathForFilesException(String message, Throwable cause) {
        super(message, cause);

    }

}
