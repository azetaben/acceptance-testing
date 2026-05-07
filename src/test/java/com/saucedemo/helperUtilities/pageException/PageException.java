package com.saucedemo.helperutilities.pageexception;

import java.io.Serial;

public class PageException extends Exception {

    @Serial
    private static final long serialVersionUID = 4699775993150102769L;

    public PageException(String message) {
        super(message);

    }
}
