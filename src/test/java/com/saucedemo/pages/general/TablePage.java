package com.saucedemo.pages.general;


import com.saucedemo.enums.WebElements;
import com.saucedemo.exceptions.TableRowDoesNotExistException;

import java.util.Map;

public interface TablePage {

    void assertTableHasElementInEveryRow(WebElements deleteLink);

    void assertCorrectTableContents(Map<String, String> fieldDataFromPage) throws TableRowDoesNotExistException;
}
