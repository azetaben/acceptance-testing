package com.saucedemo.helperUtilities.javaScream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public class JavaScriptExecutor {
    private static final Logger log = LogManager.getLogger(JavaScriptExecutor.class);
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    private WebElement element;

    public JavaScriptExecutor(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    // 1. Scroll to the bottom of the page
    public void scrollToBottom() {
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        log.info("Scrolled to the bottom of the page");
    }

    // 2. Scroll to the top of the page
    public void scrollToTop() {
        jsExecutor.executeScript("window.scrollTo(0, 0);");
        log.info("Scrolled to the top of the page");
    }

    // 3. Scroll to a specific element
    public void scrollToElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // 4. Click an element using JavaScript
    public void clickElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    // 5. Get the text of an element using JavaScript
    public String getTextFromElement(WebElement element) {
        log.info("Getting text from element: " + element.getText());
        return (String) jsExecutor.executeScript("return arguments[0].innerText;", element);


    }

    // 6. Set the value of an input field
    public void setInputValue(WebElement element, String value) {
        jsExecutor.executeScript("arguments[0].value = arguments[1];", element, value);
        log.info("Setting value of input field: " + value);
    }

    // 7. Get the value of an input field
    public String getInputValue(WebElement element) {
        log.info("Getting value of input field: " + element.getDomAttribute("value"));
        return (String) jsExecutor.executeScript("return arguments[0].value;", element);
    }

    // 8. Highlight an element
    public void highlightElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].style.border='3px solid red';", element);
        log.info("Highlighting element: " + element.getText());
    }

    // 9. Remove highlight from an element
    public void removeHighlight(WebElement element) {
        jsExecutor.executeScript("arguments[0].style.border='';", element);
        log.info("Removing highlight from element: " + element.getText());
    }

    // 10. Get the attribute of an element
    public String getAttribute(WebElement element, String attribute) {
        log.info("Getting attribute '" + attribute + "' from element: " + element.getDomAttribute(attribute));
        return (String) jsExecutor.executeScript("return arguments[0].getAttribute(arguments[1]);", element, attribute);
    }

    // 11. Set an attribute of an element
    public void setAttribute(WebElement element, String attribute, String value) {
        log.info("Setting attribute '" + attribute + "' to value '" + value + "' for element: " + element.getText());
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attribute, value);
    }

    // 12. Remove an attribute from an element
    public void removeAttribute(WebElement element, String attribute) {
        jsExecutor.executeScript("arguments[0].removeAttribute(arguments[1]);", element, attribute);
        log.info("Removing attribute '" + attribute + "' from element: " + element.getText());
    }

    // 13. Get the CSS property value of an element
    public String getCssValue(WebElement element, String property) {
        log.info("Getting CSS property '" + property + "' from element: " + element.getText());
        return (String) jsExecutor.executeScript("return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", element, property);
    }

    // 14. Execute a custom JavaScript function
    public Object executeJavaScript(String script, Object... args) {
        log.info("Executing custom JavaScript function: " + script);
        return jsExecutor.executeScript(script, args);
    }

    // 15. Get the page title
    public String getPageTitle() {
        log.info("Getting page title: " + Objects.requireNonNull(jsExecutor.executeScript("return document.title;")).toString());
        return Objects.requireNonNull(jsExecutor.executeScript("return document.title;")).toString();
    }

    // 16. Get the current URL
    public String getCurrentUrl() {
        log.info("Getting current URL: " + Objects.requireNonNull(jsExecutor.executeScript("return window.location.href;")).toString());
        return Objects.requireNonNull(jsExecutor.executeScript("return window.location.href;")).toString();
    }

    // 17. Refresh the page
    public void refreshPage() {
        jsExecutor.executeScript("location.reload();");
        log.info("Refreshing the page");
    }

    // 18. Navigate to a specific URL
    public void navigateToUrl(String url) {
        jsExecutor.executeScript("window.location.href = arguments[0];", url);
        log.info("Navigating to URL: " + url);
    }

    // 19. Get all links on the page
    public List<String> getAllLinks() {
        return (List<String>) jsExecutor.executeScript(
                "return Array.from(document.getElementsByTagName('a')).map(a => a.href);"
        );
    }

    // 20. Get all images on the page
    public List<String> getAllImages() {
        return (List<String>) jsExecutor.executeScript(
                "return Array.from(document.getElementsByTagName('img')).map(img => img.src);"
        );
    }

    // 21. Get the inner HTML of an element
    public String getInnerHTML(WebElement element) {
        log.info("Getting inner HTML of element: " + element.getText());
        return (String) jsExecutor.executeScript("return arguments[0].innerHTML;", element);
    }

    // 22. Get the outer HTML of an element
    public String getOuterHTML(WebElement element) {
        log.info("Getting outer HTML of element: " + element.getText());
        return (String) jsExecutor.executeScript("return arguments[0].outerHTML;", element);
    }

    // 23. Wait for an element to be visible using JavaScript
    public void waitForElementToBeVisible(WebElement element) {
        jsExecutor.executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                        "var element = arguments[0];" +
                        "var observer = new MutationObserver(function(mutations) {" +
                        "  if (element.offsetParent !== null) {" +
                        "    observer.disconnect();" +
                        "    callback();" +
                        "  }" +
                        "});" +
                        "observer.observe(document.body, { childList: true, subtree: true });" +
                        "if (element.offsetParent !== null) callback();", element);
    }

    // 24. Scroll to an element and click
    public void scrollToElementAndClick(WebElement element) {
        scrollToElement(element);
        log.info("Scrolling to element and clicking: " + element.getText());
        log.info("Clicked on element: " + element.getText());
        clickElement(element);

    }

    // 25. Get the dimensions of an element
    public int[] getElementDimensions(WebElement element) {
        log.info("Getting dimensions of element: " + element.getText());
        return (int[]) jsExecutor.executeScript("return [arguments[0].offsetWidth, arguments[0].offsetHeight];", element);
    }

    // 26. Get the position of an element
    public int[] getElementPosition(WebElement element) {
        log.info("Getting position of element: " + element.getText());
        return (int[]) jsExecutor.executeScript("return [arguments[0].getBoundingClientRect().left, arguments[0].getBoundingClientRect().top];", element);
    }

    // 27. Check if an element is visible
    public boolean isElementVisible(WebElement element) {
        log.info("Checking if element is visible: " + element.getText());
        return (Boolean) jsExecutor.executeScript("return !!(arguments[0].offsetWidth || arguments[0].offsetHeight || arguments[0].getClientRects().length);", element);
    }

    // 28. Get the number of child elements of a parent element
    public int getChildElementCount(WebElement parentElement) {
        log.info("Getting number of child elements of parent element: " + parentElement.getText());
        return ((Long) jsExecutor.executeScript("return arguments[0].children.length;", parentElement)).intValue();
    }

    // 29. Get all child elements of a parent element
    public List<WebElement> getChildElements(WebElement parentElement) {
        log.info("Getting all child elements of parent element: " + parentElement.getText());
        return (List<WebElement>) jsExecutor.executeScript("return Array.from(arguments[0].children);", parentElement);
    }

    // 30. Get the first child element of a parent element
    public WebElement getFirstChildElement(WebElement parentElement) {
        log.info("Getting first child element of parent element: " + parentElement.getText());
        return (WebElement) jsExecutor.executeScript("return arguments[0].firstElementChild;", parentElement);
    }

    // 31. Get the last child element of a parent element
    public WebElement getLastChildElement(WebElement parentElement) {
        log.info("Getting last child element of parent element: " + parentElement.getText());
        return (WebElement) jsExecutor.executeScript("return arguments[0].lastElementChild;", parentElement);
    }

    // 32. Get the previous sibling of an element
    public WebElement getPreviousSibling(WebElement element) {
        log.info("Getting previous sibling of element: " + element.getText());
        return (WebElement) jsExecutor.executeScript("return arguments[0].previousElementSibling;", element);
    }

    // 33. Get the next sibling of an element
    public WebElement getNextSibling(WebElement element) {
        log.info("Getting next sibling of element: " + element.getText());
        return (WebElement) jsExecutor.executeScript("return arguments[0].nextElementSibling;", element);
    }

    // 34. Get the computed style of an element
    public String getComputedStyle(WebElement element, String property) {
        log.info("Getting computed style of element: " + element.getText());
        return (String) jsExecutor.executeScript("return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", element, property);
    }

    // 35. Set the display style of an element
    public void setDisplayStyle(WebElement element, String display) {
        log.info("Setting display style of element: " + element.getText());
        jsExecutor.executeScript("arguments[0].style.display = arguments[1];", element, display);
    }

    // 36. Get the scroll position of the page
    public int[] getScrollPosition() {
        log.info("Getting scroll position of the page");
        return (int[]) jsExecutor.executeScript("return [window.scrollX, window.scrollY];");
    }

    // 37. Set the scroll position of the page
    public void setScrollPosition(int x, int y) {
        log.info("Setting scroll position of the page");
        jsExecutor.executeScript("window.scrollTo(arguments[0], arguments[1]);", x, y);
    }

    // 38. Get the current window size
    public int[] getWindowSize() {
        log.info("Getting current window size");
        return (int[]) jsExecutor.executeScript("return [window.innerWidth, window.innerHeight];");
    }

    // 39. Set the window size
    public void setWindowSize(int width, int height) {
        log.info("Setting window size");
        jsExecutor.executeScript("window.resizeTo(arguments[0], arguments[1]);", width, height);
    }

    // 40. Get the current document
    public Object getCurrentDocument() {
        log.info("Getting current document");
        return jsExecutor.executeScript("return document;");
    }

    // 41. Get the current location object
    public Object getCurrentLocation() {
        log.info("Getting current location");
        return jsExecutor.executeScript("return window.location;");
    }

    // 42. Get the current history object
    public Object getCurrentHistory() {
        log.info("Getting current history");
        return jsExecutor.executeScript("return window.history;");
    }

    // 43. Get the current navigator object
    public Object getCurrentNavigator() {
        return jsExecutor.executeScript("return window.navigator;");
    }

    // 44. Get the current performance object
    public Object getCurrentPerformance() {
        log.info("Getting current performance");
        return jsExecutor.executeScript("return window.performance;");
    }

    // 45. Execute a custom JavaScript function with arguments
    public Object executeCustomFunction(String functionName, Object... args) {
        StringBuilder script = new StringBuilder(functionName + "(");
        for (int i = 0; i < args.length; i++) {
            script.append("arguments[").append(i).append("]");
            if (i < args.length - 1) {
                script.append(", ");
            }
        }
        script.append(");");
        return jsExecutor.executeScript(script.toString(), args);
    }
}
