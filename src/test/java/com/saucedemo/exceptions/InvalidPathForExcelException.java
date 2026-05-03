package com.saucedemo.exceptions;

public class InvalidPathForExcelException extends InvalidPathForFilesException {

    private static final long serialVersionUID = 1L;

    public InvalidPathForExcelException(String message) {
        super(message);
    }

    public InvalidPathForExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
