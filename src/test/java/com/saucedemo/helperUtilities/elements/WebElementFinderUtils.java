package com.saucedemo.helperUtilities.elements;

import com.saucedemo.exceptions.TableRowDoesNotExistException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class WebElementFinderUtils {
    private static WebDriver driver;

    public static List<WebElement> findAllChildElementsOfParentElementInActualOrder(String idOfParentElement) {
        WebElement parentWebElement = driver.findElement(By.id(idOfParentElement));
        return parentWebElement.findElements(By.xpath(".//*"));
    }

    public static WebElement findTableRow(String idOfTableBodyElement, int rowNumber) throws TableRowDoesNotExistException {
        List<WebElement> rowsInActualOrder = findAllTableRows(idOfTableBodyElement);
        TableRowDoesNotExistException.checkIfTableRowExistsOrElseThrowException("The specified row does not exist within the Table.", rowsInActualOrder, rowNumber);
        return !rowsInActualOrder.isEmpty() ? rowsInActualOrder.get(rowNumber) : null;
    }

    public static List<WebElement> findAllTableRows(String idOfTableBodyElement) {
        List<WebElement> childWebElementsInActualOrder = findAllChildElementsOfParentElementInActualOrder(idOfTableBodyElement);
        return childWebElementsInActualOrder.stream()
                .filter(element -> "tr".equals(element.getTagName()))
                .collect(Collectors.toList());
    }

    public static List<WebElement> findAllChildElementsOfParentElementInActualOrder(WebElement parentWebElement) {
        List<WebElement> childWebElementsInActualOrder = parentWebElement.findElements(By.xpath(".//*"));
        String tagName = childWebElementsInActualOrder.get(childWebElementsInActualOrder.size() - 1).getText();
        return childWebElementsInActualOrder;
    }
}
