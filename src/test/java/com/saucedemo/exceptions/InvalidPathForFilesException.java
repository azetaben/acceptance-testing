package com.saucedemo.exceptions;

import java.io.Serial;

public class InvalidPathForFilesException extends FrameworkException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPathForFilesException(String message) {
        super(message);
    }

    public InvalidPathForFilesException(String message, Throwable cause) {
        super(message, cause);
    }
}
