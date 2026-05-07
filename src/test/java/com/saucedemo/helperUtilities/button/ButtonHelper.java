package com.saucedemo.helperutilities.button;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperutilities.globalvar.GlobalVarsHelper;
import com.saucedemo.helperutilities.logger.LoggerHelper;
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


    public void click(WebElement element) {
        try {
            element.click();
            log.info("Clicked on element: " + element);

        } catch (Exception e) {
            log.error("Error clicking on element: " + element, e);

        }
    }


    public void click(By locator) {
        try {
            WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
            if (element == null) {
                throw new NoSuchElementException("Element not clickable: " + locator);
            }
            element.click();
            log.info("Clicked on element with locator: " + locator);

        } catch (NoSuchElementException e) {
            log.error("Element not found with locator: " + locator, e);

        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator, e);

        }
    }


    public void clickWithWait(By locator, int timeoutInSeconds) {
        try {
            WebElement element = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, locator);
            if (element == null) {
                throw new NoSuchElementException("Element not clickable after wait: " + locator);
            }
            element.click();
            log.info("Clicked on element with locator: " + locator + " after waiting.");

        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator + " after waiting.", e);
        }
    }


    public void clickWithWait(WebElement element, int timeoutInSeconds) {
        try {
            WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            if (clickableElement == null) {
                throw new NoSuchElementException("Element not clickable after wait: " + element);
            }
            clickableElement.click();
            log.info("Clicked on element: " + element + " after waiting.");

        } catch (Exception e) {
            log.error("Error clicking on element: " + element + " after waiting.", e);

        }
    }


    public void clickWithDefaultWait(By locator) {
        clickWithWait(locator, GlobalVarsHelper.getDefaultExplicitTimeout());
    }


    public void clickWithDefaultWait(WebElement element) {
        clickWithWait(element, GlobalVarsHelper.getExplicitTimeout());
    }


    public void clickWithJS(WebElement element) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
            log.info("Clicked on element with JavaScript: " + element);

        } catch (Exception e) {
            log.error("Error clicking on element with JavaScript: " + element, e);

        }
    }


    public void clickWithActions(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
            log.info("Clicked on element with Actions class: " + element);

        } catch (Exception e) {
            log.error("Error clicking on element with Actions class: " + element, e);

        }
    }


    public void clickElementByText(List<WebElement> elements, String text) {
        try {
            for (WebElement element : elements) {
                if (element.getText().trim().equals(text)) {
                    clickWithDefaultWait(element);
                    log.info("Clicked on element with text: '" + text + "'");

                    return;
                }
            }
            log.warn("No element found with text: '" + text + "'");

        } catch (Exception e) {
            log.error("Error clicking on element with text: '" + text + "'", e);

        }
    }


    public void clickElementByText(By locator, String text) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            clickElementByText(elements, text);
        } catch (Exception e) {
            log.error("Error clicking on element with text: '" + text + "' and locator: " + locator, e);
        }
    }


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

                return;
            } catch (Exception e) {
                log.error("Error clicking on element with locator: " + locator, e);

                return;
            }
        }
        log.error("Failed to click on element with locator: " + locator + " after " + maxRetries + " retries.");
    }


    public void clickWithRetry(By locator) {
        clickWithRetry(locator, 3);
    }


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


    public void clickWithWaitAndJS(By locator) {
        clickWithWaitAndJS(locator, GlobalVarsHelper.getExplicitTimeout());
    }


    public void clickWithWaitAndJS(WebElement element) {
        clickWithWaitAndJS(element, GlobalVarsHelper.getExplicitTimeout());
    }


    public void clickWithWaitStrategy(By locator, WaitStrategy waitStrategy) {
        WebElement element = null;
        try {
            element = ExplicitWaitFactory.performExplicitWait(waitStrategy, locator);
            log.info("Waiting for element with strategy: " + waitStrategy + " | locator: " + locator);
            if (element != null) {
                element.click();
                log.info("Clicked on element: " + locator);

            }
        } catch (Exception e) {
            log.error("Error clicking on element with locator: " + locator + " and wait strategy: " + waitStrategy, e);
        }
    }


    public void clickWithWait(WebElement element) {
        try {
            WebElement clickableElement = ExplicitWaitFactory.performExplicitWait(WaitStrategy.CLICKABLE, element);
            if (clickableElement != null) clickableElement.click();
            log.info("Clicked on element: " + element + " after waiting.");
        } catch (Exception e) {
            log.error("Error clicking on element: " + element + " after waiting.", e);
        }
    }


    public void clickWithActionsAndMove(WebElement element) {
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            actions.click().perform();
            log.info("Moved to and clicked on element with Actions class: " + element);

        } catch (Exception e) {
            log.error("Error moving to and clicking on element with Actions class: " + element, e);
        }
    }


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


    public void clickWithDefaultWait(List<WebElement> elements) {
        clickWithWait(elements, GlobalVarsHelper.getExplicitTimeout());
    }


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


    public void clickWithWaitAndJS(List<WebElement> elements) {
        clickWithWaitAndJS(elements, GlobalVarsHelper.getExplicitTimeout());
    }
}
