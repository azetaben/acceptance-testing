package com.saucedemo.exceptions;

import org.openqa.selenium.WebElement;

import java.io.Serial;
import java.util.List;

public class TableRowDoesNotExistException extends FrameworkException {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MESSAGE = "The specified row does not exist within the table.";

    public TableRowDoesNotExistException(String message) {
        super(message);
    }

    public TableRowDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void checkIfTableRowExistsOrElseThrowException(String message,
                                                                 List<WebElement> rows,
                                                                 int rowIndex) {
        if (rows == null || rows.isEmpty()) {
            throw new TableRowDoesNotExistException(resolveMessage(message));
        }

        if (rowIndex < 0 || rowIndex >= rows.size()) {
            throw new TableRowDoesNotExistException(resolveMessage(message));
        }

        if (rows.get(rowIndex) == null) {
            throw new TableRowDoesNotExistException(resolveMessage(message));
        }
    }

    private static String resolveMessage(String message) {
        return (message == null || message.trim().isEmpty()) ? DEFAULT_MESSAGE : message;
    }
}
