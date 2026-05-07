package com.saucedemo.helperutilities.assertorOrdering;

import com.saucedemo.helperutilities.elements.WebElementFinderUtils;
import com.saucedemo.helperutilities.webelement.WebElementOrderChecker;
import com.saucedemo.helperutilities.webelement.WebElementOrderCheckerImpl;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class WebElementOrderingAssertor {

    private String idOfParentWebElement;

    public WebElementOrderingAssertor(String idOfParentWebElement) {
        this.idOfParentWebElement = idOfParentWebElement;
    }

    public WebElementOrderingAssertor() {}

    public void assertOnOrderOfWebElementsByListOrder(List<String> expectedOrderOfElementIds) {
        List<WebElement> childWebElementsInActualOrder = WebElementFinderUtils.findAllChildElementsOfParentElementInActualOrder(idOfParentWebElement);
        assertOnWebElementOrder(childWebElementsInActualOrder, expectedOrderOfElementIds);
    }

    private void assertOnWebElementOrder(List<WebElement> childWebElementsInActualOrder, List<String> expectedOrderOfElementIds) {
        WebElementOrderChecker webElementOrderChecker = new WebElementOrderCheckerImpl(childWebElementsInActualOrder);
        Assert.assertTrue(webElementOrderChecker.areWebElementsOrderedLikeSpecifiedListByElementId(getModifiableIdListOfExpectedChildElements(expectedOrderOfElementIds)));
    }

    private List<String> getModifiableIdListOfExpectedChildElements(List<String> elementIds) {
        return new ArrayList<>(elementIds);
    }

    public void assertOnOrderOfWebElementsByListOrder(List<String> expectedOrderOfElementIds, WebElement parentElement) {
        List<WebElement> childWebElementsInActualOrder = WebElementFinderUtils.findAllChildElementsOfParentElementInActualOrder(parentElement);
        assertOnWebElementOrder(childWebElementsInActualOrder, expectedOrderOfElementIds);
    }

}
