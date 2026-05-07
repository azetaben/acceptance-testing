package com.saucedemo.tests.selenium4features;

import com.saucedemo.constants.FrameworkConstants;
import com.saucedemo.constants.SauceDemoConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class RelativeLocatorsDemoTest extends Selenium4FeatureBaseTest {

    @BeforeMethod
    public void setUp() {
        driver = createChromeDriver();
    }

    @Test(description = "Selenium 4 relative locators: above/below/leftOf/rightOf/near")
    public void shouldLocateElementsUsingRelativeLocators() throws InterruptedException {
        Assert.assertNotNull(baseUrl);
        driver.get(baseUrl);

        WebElement username = driver.findElement(By.id(SauceDemoConstants.LOGIN_USERNAME_FIELD_ID));
        WebElement passwordBelowUsername = driver.findElement(with(By.tagName("input")).below(username));
        Assert.assertEquals(passwordBelowUsername.getDomAttribute("id"), SauceDemoConstants.LOGIN_PASSWORD_FIELD_ID, "Expected password input below username input.");

        loginToSauceDemo(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.getExplicitWait()));
        WebElement appLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("app_logo")));
        WebElement cartContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_container")));
        WebElement productsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
        WebElement inventoryList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        WebElement sortDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("product_sort_container")));

        List<WebElement> leftCandidates = driver.findElements(with(By.className("app_logo")).toLeftOf(cartContainer));
        if (leftCandidates.isEmpty()) {
            Assert.assertTrue(appLogo.getLocation().getX() < cartContainer.getLocation().getX(),
                    "Expected app logo to be left of cart container.");
        }

        List<WebElement> rightCandidates = driver.findElements(with(By.className("shopping_cart_container")).toRightOf(appLogo));
        if (rightCandidates.isEmpty()) {
            Assert.assertTrue(cartContainer.getLocation().getX() > appLogo.getLocation().getX(),
                    "Expected cart container to be right of app logo.");
        }

        List<WebElement> aboveCandidates = driver.findElements(with(By.className("title")).above(By.className("inventory_list")));
        if (aboveCandidates.isEmpty()) {
            Assert.assertTrue(productsTitle.getLocation().getY() < inventoryList.getLocation().getY(),
                    "Expected products title to be above inventory list.");
        }

        List<WebElement> nearCandidates = driver.findElements(with(By.className("product_sort_container")).near(cartContainer, 700));
        if (nearCandidates.isEmpty()) {
            Point sortPoint = sortDropdown.getLocation();
            Point cartPoint = cartContainer.getLocation();
            int dx = Math.abs(sortPoint.getX() - cartPoint.getX());
            int dy = Math.abs(sortPoint.getY() - cartPoint.getY());
            Assert.assertTrue(dx <= 700 && dy <= 700, "Expected sort dropdown near cart container.");
        }
    }
}
