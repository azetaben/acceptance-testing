package com.saucedemo.pages.general;


import com.saucedemo.enums.WebElements;

public interface OrderedPage {

    void assertIfElementsAreOrderedAsInTheExpectedList(WebElements[] expectedWebElements);

}
