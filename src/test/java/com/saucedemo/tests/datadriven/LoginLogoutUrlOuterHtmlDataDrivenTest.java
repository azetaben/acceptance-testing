package com.saucedemo.tests.datadriven;

import com.saucedemo.configReader.FrameworkConfig;
import com.saucedemo.constants.SauceDemoConstants;
import com.saucedemo.pages.*;
import com.saucedemo.utils.PathUtil;
import com.saucedemo.webdriverutilities.WebDrv;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LoginLogoutUrlOuterHtmlDataDrivenTest {
    private static final String EDGE_MATRIX_FILE = PathUtil.getTestDataCsvFilePath("login_logout_edge_url_outerhtml_matrix.csv");
    private static final String BASE_URL = FrameworkConfig.getInstance().getString(
            "useruitesting.url",
            FrameworkConfig.getInstance().getString(
                    "uitesting.url",
                    FrameworkConfig.getInstance().getString("url", "https://www.saucedemo.com/")
            )
    );

    @BeforeMethod
    public void setUp() {
        WebDrv.getInstance().openBrowser(BASE_URL);
    }

    @AfterMethod(alwaysRun = true, timeOut = 30000)
    public void tearDown() {
        PageManager.getInstance().clear();
        WebDrv.getInstance().closeCurrentDriver();
    }

    @DataProvider(name = "edgeMatrix")
    public Object[][] edgeMatrix() throws IOException {
        List<EdgeMatrixRow> rows = readRows();
        Object[][] matrix = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            matrix[i][0] = rows.get(i);
        }
        return matrix;
    }

    @Test(dataProvider = "edgeMatrix")
    public void loginLogoutEdgeUrlOuterHtmlCoverage(EdgeMatrixRow row) {
        LoginPage loginPage = PageManager.getInstance().getPage(LoginPage.class);

        switch (row.flow()) {
            case "LOGIN_SUCCESS" -> {
                loginPage.enterUsername(row.username());
                loginPage.enterPassword(row.password());
                loginPage.clickLoginButton();
            }
            case "LOGIN_FAILURE" -> {
                loginPage.enterUsername(row.username());
                loginPage.enterPassword(row.password());
                loginPage.clickLoginButton();
            }
            case "LOGIN_FAILURE_EMPTY" -> {
                loginPage.clearUsername();
                loginPage.clearPassword();
                loginPage.clickLoginButton();
            }
            case "LOGOUT_AFTER_LOGIN" -> {
                loginPage.enterUsername(row.username());
                loginPage.enterPassword(row.password());
                loginPage.clickLoginButton();
                TopNavigationLinksPage topNavigationLinksPage = PageManager.getInstance().getPage(TopNavigationLinksPage.class);
                TogglePage togglePage = topNavigationLinksPage.clickMenuIcon();
                togglePage.clickOnTopLeftMenuItem(SauceDemoConstants.MENU_ITEM_LOGOUT);
            }
            default -> throw new IllegalArgumentException("Unsupported flow in matrix: " + row.flow());
        }

        String currentUrl = WebDrv.getInstance().getWebDriver().getCurrentUrl();
        Assert.assertTrue(urlContainsOrEquivalent(currentUrl, row.expectedUrlContains()),
                row.testCaseId() + " expected URL to contain '" + row.expectedUrlContains() + "' but was '" + currentUrl + "'.");

        String outerHtml = resolveOuterHtml(row.outerHtmlTarget());
        Assert.assertTrue(outerHtml.contains(row.expectedOuterHtmlContains()),
                row.testCaseId() + " expected outerHtml target '" + row.outerHtmlTarget() + "' to contain '"
                        + row.expectedOuterHtmlContains() + "'.");
    }

    private String resolveOuterHtml(String target) {
        return switch (target) {
            case "loginForm" -> PageManager.getInstance().getPage(LoginPage.class).getLoginFormOuterHtml();
            case "loginError" -> PageManager.getInstance().getPage(LoginPage.class).getLoginErrorOuterHtml();
            case "inventoryList" -> PageManager.getInstance().getPage(InventoryPage.class).getInventoryListOuterHtml();
            default -> throw new IllegalArgumentException("Unsupported outerHtml target: " + target);
        };
    }

    private boolean urlContainsOrEquivalent(String actualUrl, String expectedContains) {
        if (actualUrl == null) {
            return false;
        }
        if (expectedContains == null || expectedContains.isBlank()) {
            return true;
        }

        if (actualUrl.contains(expectedContains)) {
            return true;
        }

        // Treat "index.html" as equivalent to the site root in cases where the app redirects.
        if (expectedContains.contains("index.html")) {
            try {
                URI uri = new URI(actualUrl);
                String path = uri.getPath();
                if (path == null || path.isBlank() || "/".equals(path)) {
                    return true;
                }
            } catch (URISyntaxException ignored) {
                // Fall through and fail assertion.
            }
        }

        return false;
    }

    private List<EdgeMatrixRow> readRows() throws IOException {
        List<String> lines = Files.readAllLines(Path.of(EDGE_MATRIX_FILE));
        List<EdgeMatrixRow> rows = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(",", -1);
            if (parts.length < 7) {
                throw new IllegalArgumentException("Invalid matrix row at line " + (i + 1) + ": " + line);
            }

            rows.add(new EdgeMatrixRow(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim(),
                    parts[5].trim(),
                    parts[6].trim()
            ));
        }
        return rows;
    }

    private record EdgeMatrixRow(
            String testCaseId,
            String flow,
            String username,
            String password,
            String expectedUrlContains,
            String outerHtmlTarget,
            String expectedOuterHtmlContains
    ) {
    }
}
