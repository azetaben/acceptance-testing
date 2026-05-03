package com.saucedemo.helperUtilities.button;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import com.saucedemo.helperUtilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class ButtonHelper {
    private final Logger log = LoggerHelper.getLogger(ButtonHelper.class);
    private final WebDriver driver;

    public ButtonHelper(WebDriver driver) {
        this.driver = driver;
        ExplicitWaitFactory.setDriver(driver);
        log.debug("ButtonHelper initialized with driver: " + driver.hashCode());
    }

    // 1. Basic Click on WebElement
    public void click(WebElement element) {
        try {
            element.click();
            log.info("Clicked on element: " + element);
            // test.log(Status.INFO, "Clicked on element: " + element);
        } catch (Exception e) {
            log.error("Error clicking on element: " + element, e);
            // test.log(Status.ERROR, "Error clicking on element: " + element);
        }
    }

    // 2. Click on Element by Locator
    public void click(By locator) {
        try {
            WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
            if (element == null) {
                throw new NoSuchElementException("Element not clickable: " + locator);
            }
            element.click();
            log.info("Clicked on element with locator: " + locator);
            // test.log(Status.INFO, "Clicked on element with locator: " + locator);
        } catch (NoSuchElementException e) {
            log.error("Element not found with locator: " + locator, e);
            // test.log(Status.ERROR, "Element not found with locator: " + locator);
        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator, e);
            // test.log(Status.ERROR, "Error clicking on element with locator: " + locator);
        }
    }

    // 3. Click with Explicit Wait (Clickable) on By
    public void clickWithWait(By locator, int timeoutInSeconds) {
        try {
            WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
            if (element == null) {
                throw new NoSuchElementException("Element not clickable after wait: " + locator);
            }
            element.click();
            log.info("Clicked on element with locator: " + locator + " after waiting.");
            // test.log(Status.INFO, "Clicked on element with locator: " + locator + " after waiting.");
        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator + " after waiting.", e);
        }
    }

    // 3.1 Click with Explicit Wait (Clickable) on WebElement
    public void clickWithWait(WebElement element, int timeoutInSeconds) {
        try {
            WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            if (clickableElement == null) {
                throw new NoSuchElementException("Element not clickable after wait: " + element);
            }
            clickableElement.click();
            log.info("Clicked on element: " + element + " after waiting.");
            // test.log(Status.INFO, "Clicked on element: " + element + " after waiting.");
        } catch (Exception e) {
            log.error("Error clicking on element: " + element + " after waiting.", e);
            // test.log(Status.ERROR, "Error clicking on element: " + element + " after waiting.");
        }
    }

    // 4. Click with Default Explicit Wait (Clickable) on By
    public void clickWithDefaultWait(By locator) {
        clickWithWait(locator, GlobalVarsHelper.getDefaultExplicitTimeout());
    }

    // 4.1 Click with Default Explicit Wait (Clickable) on WebElement
    public void clickWithDefaultWait(WebElement element) {
        clickWithWait(element, GlobalVarsHelper.getExplicitTimeout());
    }

    // 5. Click with JavaScript Executor
    public void clickWithJS(WebElement element) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            log.info("Clicked on element with JavaScript: " + element);
            // test.log(Status.INFO, "Clicked on element with JavaScript: " + element);
        } catch (Exception e) {
            log.error("Error clicking on element with JavaScript: " + element, e);
            // test.log(Status.ERROR, "Error clicking on element with JavaScript: " + element);
        }
    }

    // 6. Click with Actions Class (moveToElement)
    public void clickWithActions(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
            log.info("Clicked on element with Actions class: " + element);
            // test.log(Status.INFO, "Clicked on element with Actions class: " + element);
        } catch (Exception e) {
            log.error("Error clicking on element with Actions class: " + element, e);
            // test.log(Status.ERROR, "Error clicking on element with Actions class: " + element);
        }
    }

    // 7. Click on Element by Text (List of Elements)
    public void clickElementByText(List<WebElement> elements, String text) {
        try {
            for (WebElement element : elements) {
                if (element.getText().trim().equals(text)) {
                    clickWithDefaultWait(element);
                    log.info("Clicked on element with text: '" + text + "'");
                    // test.log(Status.INFO, "Clicked on element with text: '" + text + "'");
                    return;
                }
            }
            log.warn("No element found with text: '" + text + "'");
            // test.log(Status.WARNING, "No element found with text: '" + text + "'");
        } catch (Exception e) {
            log.error("Error clicking on element with text: '" + text + "'", e);
            // test.log(Status.ERROR, "Error clicking on element with text: '" + text + "'");
        }
    }

    // 8. Click on Element by Text (By Locator)
    public void clickElementByText(By locator, String text) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            clickElementByText(elements, text);
        } catch (Exception e) {
            log.error("Error clicking on element with text: '" + text + "' and locator: " + locator, e);
        }
    }

    // 9. Click with Retry (Stale Element Handling)
    public void clickWithRetry(By locator, int maxRetries) {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                WebElement element = driver.findElement(locator);
                element.click();
                log.info("Clicked on element with locator: " + locator + " (attempt: " + (retries + 1) + ")");
                return;
            } catch (StaleElementReferenceException e) {
                retries++;
                log.warn("Stale element encountered. Retrying... (attempt: " + retries + ")");
            } catch (NoSuchElementException e) {
                log.error("Element not found with locator: " + locator, e);
                // test.log(Status.ERROR, "Element not found with locator: " + locator);
                return;
            } catch (Exception e) {
                log.error("Error clicking on element with locator: " + locator, e);
                // test.log(Status.ERROR, "Error clicking on element with locator: " + locator);
                return;
            }
        }
        log.error("Failed to click on element with locator: " + locator + " after " + maxRetries + " retries.");
    }

    // 10. Click with Retry (Default Retries)
    public void clickWithRetry(By locator) {
        clickWithRetry(locator, 3); // Default 3 retries
    }

    // 11. Click with Wait and JS if needed
    public void clickWithWaitAndJS(By locator, int timeoutInSeconds) {
        try {
            WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
            if (element == null) throw new NoSuchElementException("Element not clickable after wait: " + locator);
            try {
                element.click();
                log.info("Clicked on element with locator: " + locator + " after waiting.");
            } catch (ElementClickInterceptedException e) {
                log.warn("ElementClickInterceptedException encountered. Trying with JS.");
                clickWithJS(element);
            }
        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator + " after waiting.", e);
        }
    }

    // 11.1 Click with Wait and JS if needed on WebElement
    public void clickWithWaitAndJS(WebElement element, int timeoutInSeconds) {
        try {
            WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            if (clickableElement == null)
                throw new NoSuchElementException("Element not clickable after wait: " + element);
            try {
                clickableElement.click();
                log.info("Clicked on element: " + element + " after waiting.");
            } catch (ElementClickInterceptedException e) {
                log.warn("ElementClickInterceptedException encountered. Trying with JS.");
                clickWithJS(clickableElement);
            }
        } catch (Exception e) {
            log.error("Error clicking on element: " + element + " after waiting.", e);
        }
    }

    // 12. Click with Wait and JS if needed (Default Wait) on By
    public void clickWithWaitAndJS(By locator) {
        clickWithWaitAndJS(locator, GlobalVarsHelper.getExplicitTimeout());
    }

    // 12.1 Click with Wait and JS if needed (Default Wait) on WebElement
    public void clickWithWaitAndJS(WebElement element) {
        clickWithWaitAndJS(element, GlobalVarsHelper.getExplicitTimeout());
    }

    // 13. Click with Wait Strategy
    public void clickWithWaitStrategy(By locator, WaitStrategy waitStrategy) {
        WebElement element = null;
        try {
            element = ExplicitWaitFactory.performExplicitWait(waitStrategy, locator);
            log.info("Waiting for element with strategy: " + waitStrategy + " | locator: " + locator);
            if (element != null) {
                element.click();
                log.info("Clicked on element: " + locator);
                // test.log(Status.INFO, "Clicked on element: " + locator);
            }
        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator + " and wait strategy: " + waitStrategy, e);
        }
    }

    // 14. Click with Wait on WebElement
    public void clickWithWait(WebElement element) {
        try {
            WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            if (clickableElement != null) clickableElement.click();
            log.info("Clicked on element: " + element + " after waiting.");
        } catch (Exception e) {
            log.error("Error clicking on element: " + element + " after waiting.", e);
        }
    }

    // 15. Click with Actions Class (moveToElement and click)
    public void clickWithActionsAndMove(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            actions.click().perform();
            log.info("Moved to and clicked on element with Actions class: " + element);
            // test.log(Status.INFO, "Moved to and clicked on element with Actions class: " + element);
        } catch (Exception e) {
            log.error("Error moving to and clicking on element with Actions class: " + element, e);
        }
    }

    // 16. Click on List of WebElement with Explicit Wait (Clickable)
    public void clickWithWait(List<WebElement> elements, int timeoutInSeconds) {
        try {
            for (WebElement element : elements) {
                WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
                if (clickableElement != null) clickableElement.click();
                log.info("Clicked on element: " + element + " after waiting.");
            }
        } catch (Exception e) {
            log.error("Error clicking on elements after waiting.", e);
        }
    }

    // 17. Click on List of WebElement with Default Explicit Wait (Clickable)
    public void clickWithDefaultWait(List<WebElement> elements) {
        clickWithWait(elements, GlobalVarsHelper.getExplicitTimeout());
    }

    // 18. Click with Wait and JS if needed on List of WebElement
    public void clickWithWaitAndJS(List<WebElement> elements, int timeoutInSeconds) {
        try {
            for (WebElement element : elements) {
                WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
                if (clickableElement == null) continue;
                try {
                    clickableElement.click();
                    log.info("Clicked on element: " + element + " after waiting.");
                } catch (ElementClickInterceptedException e) {
                    log.warn("ElementClickInterceptedException encountered. Trying with JS.");
                    clickWithJS(clickableElement);
                }
            }
        } catch (Exception e) {
            log.error("Error clicking on elements after waiting.", e);
        }
    }

    // 19. Click with Wait and JS if needed (Default Wait) on List of WebElement
    public void clickWithWaitAndJS(List<WebElement> elements) {
        clickWithWaitAndJS(elements, GlobalVarsHelper.getExplicitTimeout());
    }
}
