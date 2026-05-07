package com.saucedemo.helperutilities.actions.actiondriver;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.helperutilities.actions.actioninterface.ActionInterface;
import com.saucedemo.utils.PathUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Action implements ActionInterface {
    private static final Logger log = LogManager.getLogger(Action.class);

    @Override
    public void scrollByVisibilityOfElement(WebDriver driver, WebElement ele) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", ele);
        log.info("Scrolled to the element" + ele.getText());

    }

    @Override
    public void click(WebDriver driver, WebElement ele) {
        Actions act = new Actions(driver);
        act.moveToElement(ele).click().build().perform();
        log.info("Clicked on the element" + ele.getText());
    }

    @Override
    public boolean findElement(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            ele.isDisplayed();
            flag = true;
        } catch (Exception e) {
            log.info("Location not found");

            flag = false;
        } finally {
            if (flag) {
                log.info("Successfully Found the element");
                log.info("Successfully Found element at");

            } else {
                log.info("Unable to locate element");
                log.info("Unable to locate element at");
            }
        }
        return flag;
    }

    @Override
    public boolean isDisplayed(WebDriver driver, WebElement ele) {
        boolean flag = false;
        flag = findElement(driver, ele);
        if (flag) {
            flag = ele.isDisplayed();
            if (flag) {
                log.info("The element is Displayed");
                log.info("The element is Displayed");
            } else {
                log.info("The element is not Displayed");
                log.info("The element is not Displayed");
            }
        } else {
            log.info("Not displayed ");
            log.info("Not displayed ");
        }
        return flag;
    }

    @Override
    public boolean isSelected(WebDriver driver, WebElement ele) {
        boolean flag = false;
        flag = findElement(driver, ele);
        if (flag) {
            flag = ele.isSelected();
            if (flag) {
                log.info("The element is Selected");
                log.info("The element is Selected");
            } else {
                log.info("The element is not Selected");
                log.info("The element is not Selected");
            }
        } else {
            log.info("Not selected ");
            log.info("Not selected ");
        }
        return flag;
    }

    @Override
    public boolean isEnabled(WebDriver driver, WebElement ele) {
        boolean flag = false;
        flag = findElement(driver, ele);
        if (flag) {
            flag = ele.isEnabled();
            if (flag) {
                log.info("The element is Enabled");
                log.info("The element is Enabled");
            } else {
                log.info("The element is not Enabled");
                log.info("The element is not Enabled");
            }
        } else {
            log.info("Not Enabled ");
        }
        return flag;
    }

    @Override
    public boolean type(WebElement ele, String text) {
        boolean flag = false;
        try {
            flag = ele.isDisplayed();
            ele.clear();
            ele.sendKeys(text);
            log.info("Entered text :" + text);
            flag = true;
        } catch (Exception e) {
            log.info("Location Not found");
            log.info("Location Not found");
            flag = false;
        } finally {
            if (flag) {
                log.info("Successfully entered value");
                log.info("Successfully entered value");
            } else {
                log.info("Unable to enter value");
                log.info("Unable to enter value");
            }

        }
        return flag;
    }

    @Override
    public boolean selectBySendkeys(String value, WebElement ele) {
        boolean flag = false;
        try {
            ele.sendKeys(value);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("Select value from the DropDown");
                log.info("Select value from the DropDown");
            } else {
                log.info("Not Selected value from the DropDown");
                log.info("Not Selected value from the DropDown");

            }
        }
    }

    @Override
    public boolean selectByIndex(WebElement element, int index) {
        boolean flag = false;
        try {
            Select s = new Select(element);
            s.selectByIndex(index);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                log.info("Option selected by Index");
                log.info("Option selected by Index");
            } else {
                log.info("Option not selected by Index");
                log.info("Option not selected by Index");
            }
        }
    }


    @Override
    public boolean selectByValue(WebElement element, String value) {
        boolean flag = false;
        try {
            Select s = new Select(element);
            s.selectByValue(value);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("Option selected by Value");
                log.info("Option selected by Value");
            } else {
                log.info("Option not selected by Value");
                log.info("Option not selected by Value");
            }
        }
    }


    @Override
    public boolean selectByVisibleText(String visibletext, WebElement ele) {
        boolean flag = false;
        try {
            Select s = new Select(ele);
            s.selectByVisibleText(visibletext);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                log.info("Option selected by VisibleText");
                log.info("Option selected by VisibleText");
            } else {
                log.info("Option not selected by VisibleText");
                log.info("Option not selected by VisibleText");
            }
        }
    }

    @Override
    public boolean mouseHoverByJavaScript(WebElement locator) {
        return false;
    }

    @Override
    public boolean JSClick(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", ele);

            flag = true;
        } catch (Exception e) {
            throw e;

        } finally {
            if (flag) {
                log.info("Click Action is performed");
                log.info("Click Action is performed");
            } else if (!flag) {
                log.info("Click Action is not performed");
                log.info("Click Action is not performed");
            }
        }
        return flag;
    }

    @Override
    public boolean switchToFrameByIndex(WebDriver driver, int index) {
        boolean flag = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait())).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe")));
            driver.switchTo().frame(index);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("Frame with index \"" + index + "\" is selected");
                log.info("Frame with index \"" + index + "\" is selected");
            } else {
                log.info("Frame with index \"" + index + "\" is not selected");
                log.info("Frame with index \"" + index + "\" is not selected");
            }
        }
    }


    @Override
    public boolean switchToFrameById(WebDriver driver, String idValue) {
        boolean flag = false;
        try {
            driver.switchTo().frame(idValue);
            flag = true;
            return true;
        } catch (Exception e) {

            log.warn("Exception: " + e.getMessage(), e);
            return false;
        } finally {
            if (flag) {
                log.info("Frame with Id \"" + idValue + "\" is selected");
                log.info("Frame with Id \"" + idValue + "\" is selected");
            } else {
                log.info("Frame with Id \"" + idValue + "\" is not selected");
                log.info("Frame with Id \"" + idValue + "\" is not selected");
            }
        }
    }


    @Override
    public boolean switchToFrameByName(WebDriver driver, String nameValue) {
        boolean flag = false;
        try {
            driver.switchTo().frame(nameValue);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("Frame with Name \"" + nameValue + "\" is selected");
                log.info("Frame with Name \"" + nameValue + "\" is selected");
            } else if (!flag) {
                log.info("Frame with Name \"" + nameValue + "\" is not selected");
                log.info("Frame with Name \"" + nameValue + "\" is not selected");
            }
        }
    }

    @Override
    public boolean switchToDefaultFrame(WebDriver driver) {
        boolean flag = false;
        try {
            driver.switchTo().defaultContent();
            flag = true;
            return true;
        } catch (Exception e) {
            log.warn("Exception: " + e.getMessage(), e);
            return false;
        } finally {
            if (flag) {
                log.info("Default Frame is selected");
                log.info("Default Frame is selected");

            } else if (!flag) {
                log.info("Default Frame is not selected");
                log.info("Default Frame is not selected");

            }
        }
    }

    @Override
    public void mouseOverElement(WebDriver driver, WebElement element) {
        boolean flag = false;
        try {
            new Actions(driver).moveToElement(element).build().perform();
            flag = true;
        } catch (Exception e) {
            log.warn("Exception: " + e.getMessage(), e);
        } finally {
            if (flag) {
                log.info(" MouserOver Action is performed on ");
                log.info(" MouserOver Action is performed on ");

            } else {
                log.info("MouseOver action is not performed on");
                log.info("MouseOver action is not performed on");
            }
        }
    }

    @Override
    public boolean moveToElement(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", ele);
            Actions actions = new Actions(driver);

            actions.moveToElement(ele).build().perform();
            log.info("mouseover action is performed");
            flag = true;
        } catch (Exception e) {
            log.warn("Exception: " + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean mouseover(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            new Actions(driver).moveToElement(ele).build().perform();
            flag = true;
            log.info("mouseover action is performed");
            return true;
        } catch (Exception e) {
            return false;
        } finally {

        }
    }

    @Override
    public boolean draggable(WebDriver driver, WebElement source, int x, int y) {
        boolean flag = false;
        try {
            new Actions(driver).dragAndDropBy(source, x, y).build().perform();
            flag = true;
            return true;

        } catch (Exception e) {

            return false;

        } finally {
            if (flag) {
                log.info("Draggable Action is performed on \"" + source + "\"");
                log.info("Draggable Action is performed on \"" + source + "\"");

            } else if (!flag) {
                log.info("Draggable action is not performed on \"" + source + "\"");
                log.info("Draggable action is not performed on \"" + source + "\"");
            }
        }
    }

    @Override
    public boolean draganddrop(WebDriver driver, WebElement source, WebElement target) {
        boolean flag = false;
        try {
            new Actions(driver).dragAndDrop(source, target).perform();
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("DragAndDrop Action is performed");
                log.info("DragAndDrop Action is performed");

            } else if (!flag) {
                log.info("DragAndDrop Action is not performed");
                log.info("DragAndDrop Action is not performed");
            }
        }
    }

    @Override
    public boolean slider(WebDriver driver, WebElement ele, int x, int y) {
        boolean flag = false;
        try {


            new Actions(driver).dragAndDropBy(ele, x, y).build().perform();
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("Slider Action is performed");
                log.info("Slider Action is performed");
            } else {
                log.info("Slider Action is not performed");
                log.info("Slider Action is not performed");
            }
        }
    }

    @Override
    public boolean rightclick(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            Actions clicker = new Actions(driver);
            clicker.contextClick(ele).perform();
            flag = true;
            return true;

        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("RightClick Action is performed");
                log.info("RightClick Action is performed");
            } else {
                log.info("RightClick Action is not performed");
                log.info("RightClick Action is not performed");
            }
        }
    }

    @Override
    public boolean switchWindowByTitle(WebDriver driver, String windowTitle, int count) {
        boolean flag = false;
        try {
            Set<String> windowList = driver.getWindowHandles();

            String[] array = windowList.toArray(new String[0]);

            driver.switchTo().window(array[count - 1]);

            if (Objects.requireNonNull(driver.getTitle()).contains(windowTitle)) {
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                log.info("Navigated to the window with title");
                log.info("Navigated to the window with title");
            } else {
                log.info("The Window with title is not Selected");
                log.info("The Window with title is not Selected");
            }
        }
    }

    @Override
    public boolean switchToNewWindow(WebDriver driver) {
        boolean flag = false;
        try {

            Set<String> s = driver.getWindowHandles();
            Object popup[] = s.toArray();
            driver.switchTo().window(popup[1].toString());
            flag = true;
            return flag;
        } catch (Exception e) {
            flag = false;
            return flag;
        } finally {
            if (flag) {
                log.info("Window is Navigated with title");
                log.info("Window is Navigated with title");
            } else {
                log.info("The Window with title: is not Selected");
                log.info("The Window with title: is not Selected");
            }
        }
    }

    @Override
    public boolean switchToOldWindow(WebDriver driver) {
        boolean flag = false;
        try {

            Set<String> s = driver.getWindowHandles();
            Object popup[] = s.toArray();
            driver.switchTo().window(popup[0].toString());
            flag = true;
            return flag;
        } catch (Exception e) {
            flag = false;
            return flag;
        } finally {
            if (flag) {
                log.info("Focus navigated to the window with title");
                log.info("Focus navigated to the window with title");
            } else {
                log.info("The Window with title: is not Selected");
                log.info("The Window with title: is not Selected");
            }
        }
    }

    @Override
    public int getColumnCount(WebElement row) {
        List<WebElement> columns = row.findElements(By.tagName("td"));
        int a = columns.size();
        log.info(columns.size());
        for (WebElement column : columns) {
            log.info(String.valueOf(column.getText()));
            log.info(String.valueOf("|"));
        }
        return a;
    }

    @Override
    public int getRowCount(WebElement table) {
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        return rows.size() - 1;
    }

    @Override
    public boolean Alert(WebDriver driver) {
        boolean presentFlag = false;
        Alert alert = null;

        try {

            alert = driver.switchTo().alert();

            alert.accept();
            presentFlag = true;
        } catch (NoAlertPresentException ex) {


            log.warn("Exception: " + ex.getMessage(), ex);
        } finally {
            if (!presentFlag) {
                log.info("The Alert is handled successfully");
                log.info("The Alert is handled successfully");
            } else {
                log.info("There was no alert to handle");
                log.info("There was no alert to handle");
            }
        }

        return presentFlag;
    }

    @Override
    public boolean launchUrl(WebDriver driver, String url) {
        boolean flag = false;
        try {
            driver.navigate().to(url);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                log.info("Successfully launched \"" + url + "\"");
                log.info("Successfully launched \"" + url + "\"");
            } else {
                log.info("Failed to launch \"" + url + "\"");
                log.info("Failed to launch \"" + url + "\"");
            }
        }
    }

    @Override
    public boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    @Override
    public String getTitle(WebDriver driver) {
        boolean flag = false;

        String text = driver.getTitle();
        if (flag) {
            log.info("Title of the page is: \"" + text + "\"");
            log.info("Title of the page is: \"" + text + "\"");
        }
        return text;
    }

    @Override
    public String getCurrentURL(WebDriver driver) {
        boolean flag = false;

        String text = driver.getCurrentUrl();
        if (flag) {
            log.info("Current URL is: \"" + text + "\"");
            log.info("Current URL is: \"" + text + "\"");
        }
        return text;
    }

    @Override
    public boolean click1(WebElement locator, String locatorName) {
        boolean flag = false;
        try {
            locator.click();
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                log.info("Able to click on \"" + locatorName + "\"");
                log.info("Able to click on \"" + locatorName + "\"");
            } else {
                log.info("Click Unable to click on \"" + locatorName + "\"");
                log.info("Click Unable to click on \"" + locatorName + "\"");
            }
        }

    }

    @Override
    public void fluentWait(WebDriver driver, WebElement element, int timeOut) {
        Wait<WebDriver> wait = null;
        try {
            wait = new FluentWait<WebDriver>((WebDriver) driver)
                    .withTimeout(Duration.ofSeconds(timeOut > 0 ? timeOut : FrameworkConstants.getExplicitWait()))
                    .pollingEvery(Duration.ofMillis(FrameworkConstants.getFluentPollIntervalMs()))
                    .ignoring(Exception.class);
            wait.until(ExpectedConditions.visibilityOf(element));
            element.click();
        } catch (Exception e) {
            log.warn("FluentWait failed for element: " + e.getMessage());
        }
    }

    @Override
    public void implicitWait(WebDriver driver, int timeOut) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut > 0 ? timeOut : FrameworkConstants.getExplicitWait()));
    }

    @Override
    public void explicitWait(WebDriver driver, WebElement element, int timeOut) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut > 0 ? timeOut : FrameworkConstants.getExplicitWait()));
        wait.until(ExpectedConditions.visibilityOf(element));
        log.info("Element is visible");
    }

    @Override
    public void pageLoadTimeOut(WebDriver driver, int timeOut) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOut > 0 ? timeOut : FrameworkConstants.getExplicitWait()));
        log.info("Page is loaded");
    }

    @Override
    public String screenShot(WebDriver driver, String filename) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String destination = PathUtil.getScreenshotsDirFileName(filename + "_" + dateName + ".png");

        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (Exception e) {
            e.getMessage();
        }
        return destination;
    }

    @Override
    public String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date());
    }

}
