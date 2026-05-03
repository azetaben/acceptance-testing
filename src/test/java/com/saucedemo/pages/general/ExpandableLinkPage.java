package com.saucedemo.pages.general;

public interface ExpandableLinkPage extends LinkPage {

    void assertCorrectExpandedContent(String expectedExpandedContent);

}
