package com.saucedemo.exceptions;

import java.io.Serial;

public class InvalidPathForExcelException extends InvalidPathForFilesException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPathForExcelException(String message) {
        super(message);
    }

    public InvalidPathForExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
