package com.saucedemo.helperUtilities.webElement;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class WebElementOrderCheckerImpl implements WebElementOrderChecker {

    List<WebElement> webElements;
    List<String> elementIds = new ArrayList<>();

    public WebElementOrderCheckerImpl(List<WebElement> webElements) {
        this.webElements = webElements;
    }

    @Override
    public boolean areWebElementsOrderedLikeSpecifiedListByElementId(List<String> expectedElementIds) {
        populateAllIdsFromActualWebElementsThatAreAlsoPresentInExpectedElementIdList(expectedElementIds);
        for (int i = 0; i < elementIds.size(); i++) {
            if (!elementIds.get(i).equals(expectedElementIds.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void populateAllIdsFromActualWebElementsThatAreAlsoPresentInExpectedElementIdList(List<String> expectedElementIds) {
        for (WebElement webElement : webElements) {
            if (expectedElementIds.contains(webElement.getDomAttribute("id"))) {
                elementIds.add(webElement.getDomAttribute("id"));
            }
        }
    }
}
