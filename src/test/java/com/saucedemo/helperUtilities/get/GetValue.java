package com.saucedemo.helperUtilities.get;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetValue {
    public List<String> getDesiredValue(List<WebElement> elements, Function<WebElement, String> getValue) {
        return elements.stream().filter(WebElement::isDisplayed).map(getValue).collect(Collectors.toList());
    }
}
