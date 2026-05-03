package com.saucedemo.helperUtilities.assertors;

import com.saucedemo.helperUtilities.elements.WebElementFinderUtils;
import com.saucedemo.helperUtilities.webElement.WebElementOrderChecker;
import com.saucedemo.helperUtilities.webElement.WebElementOrderCheckerImpl;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class WebElementOrderingAssertor {

    private String idOfParentWebElement;

    public WebElementOrderingAssertor(String idOfParentWebElement) {
        this.idOfParentWebElement = idOfParentWebElement;
    }

    public WebElementOrderingAssertor() {
        // TODO Auto-generated constructor stub
    }

    public void assertOnOrderOfWebElementsByListOrder(List<String> expectedOrderOfElementIds) {
        List<WebElement> childWebElementsInActualOrder = WebElementFinderUtils.findAllChildElementsOfParentElementInActualOrder(idOfParentWebElement);
        assertOnWebElementOrder(childWebElementsInActualOrder, expectedOrderOfElementIds);
    }

    private void assertOnWebElementOrder(List<WebElement> childWebElementsInActualOrder, List<String> expectedOrderOfElementIds) {
        WebElementOrderChecker webElementOrderChecker = new WebElementOrderCheckerImpl(childWebElementsInActualOrder);
        Assert.assertTrue(webElementOrderChecker.areWebElementsOrderedLikeSpecifiedListByElementId(getModifiableIdListOfExpectedChildElements(expectedOrderOfElementIds)));
    }

    private List<String> getModifiableIdListOfExpectedChildElements(List<String> elementIds) {
        List<String> ids = new ArrayList<>();
        for (String elementId : elementIds) {
            ids.add(elementId);
        }
        return ids;
    }

    public void assertOnOrderOfWebElementsByListOrder(List<String> expectedOrderOfElementIds, WebElement parentElement) {
        List<WebElement> childWebElementsInActualOrder = WebElementFinderUtils.findAllChildElementsOfParentElementInActualOrder(parentElement);
        assertOnWebElementOrder(childWebElementsInActualOrder, expectedOrderOfElementIds);
    }
}
