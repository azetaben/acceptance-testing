package com.saucedemo.webelementdata;

import com.saucedemo.exceptions.TableRowDoesNotExistException;
import com.saucedemo.helperUtilities.elements.WebElementFinderUtils;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TableData {

    private WebElement tableHeadersRow;
    private WebElement tableBody;

    private List<String> expectedOrderOfHeaderIds;
    private List<String> expectedHeaderTitles;
    private List<String> actualHeaderTitles = new ArrayList<String>();

    private List<String> expectedTableBodyContent;

    private List<String> expectedOrderOfTableBodyContentIds;
    private List<String> actualTableBodyContent = new ArrayList<String>();
    private List<WebElement> tableRows = new ArrayList<>();
    private List<List<String>> tableContentWithinAllRows = new ArrayList<>();

    public TableData(WebElement tableHeadersRow, WebElement tableBody, List<String> expectedOrderOfHeaderIds,
                     List<String> expectedHeaderTitles, List<String> expectedTableBodyContent,
                     List<String> expectedOrderOfTableBodyContentIds) {
        this.tableBody = tableBody;
        this.tableHeadersRow = tableHeadersRow;
        this.expectedOrderOfHeaderIds = expectedOrderOfHeaderIds;
        this.expectedHeaderTitles = expectedHeaderTitles;
        this.expectedTableBodyContent = expectedTableBodyContent;
        this.expectedOrderOfTableBodyContentIds = expectedOrderOfTableBodyContentIds;
    }

    public TableData(WebElement tableBody) {
        this.tableBody = tableBody;
    }

    public List<String> getActualTableBodyContentForRow(int rowNumber) throws TableRowDoesNotExistException {
        if (actualTableBodyContent.isEmpty()) {
            List<WebElement> childElements = WebElementFinderUtils
                    .findAllChildElementsOfParentElementInActualOrder(getTableBodyRowWebElement(rowNumber));
            childElements.stream()
                    .filter(this::childElementIsACellWithText)
                    .map(WebElement::getText)
                    .forEach(actualTableBodyContent::add);
        }

        return actualTableBodyContent;
    }

    public WebElement getTableBodyRowWebElement(int rowNumber) throws TableRowDoesNotExistException {
        return WebElementFinderUtils.findTableRow(getTableBodyId(), rowNumber);
    }

    private boolean childElementIsACellWithText(WebElement element) {
        return element.getText() != null && childElementIsACell(element);
    }

    private boolean childElementIsACell(WebElement element) {
        return element.getTagName().equals("th") || element.getTagName().equals("td");
    }

    public String getTableBodyId() {
        return tableBody.getAttribute("id");
    }

    public List<String> getActualHeaderTitles() {
        if (actualHeaderTitles.isEmpty()) {
            List<WebElement> childElements = WebElementFinderUtils
                    .findAllChildElementsOfParentElementInActualOrder(getTableHeadersRowId());
            childElements.stream()
                    .map(WebElement::getText)
                    .filter(text -> text != null)
                    .forEach(actualHeaderTitles::add);
        }

        return actualHeaderTitles;
    }

    public String getTableHeadersRowId() {
        return tableHeadersRow.getAttribute("id");
    }

    public List<String> getExpectedTableBodyContent() {
        return expectedTableBodyContent;
    }

    public List<String> getExpectedOrderOfHeaderIds() {
        return expectedOrderOfHeaderIds;
    }

    public List<String> getExpectedHeaderTitles() {
        return expectedHeaderTitles;
    }

    public List<String> getExpectedOrderOfTableBodyContentIds() {
        return expectedOrderOfTableBodyContentIds;
    }

    public List<WebElement> getAllActualTableCells() {
        List<WebElement> rows = getAllActualTableRows();
        return rows.stream().filter(this::childElementIsACell).toList();
    }

    public List<WebElement> getAllActualTableRows() {
        if (tableRows.isEmpty()) {
            tableRows = WebElementFinderUtils.findAllTableRows(getTableBodyId());
        }
        return tableRows;
    }

    public List<List<String>> getActualTableBodyContentForAllRows() throws TableRowDoesNotExistException {
        if (tableContentWithinAllRows.isEmpty()) {
            IntStream.range(0, getAllActualTableRows().size())
                    .forEach(i -> {
                        try {
                            tableContentWithinAllRows.add(getActualTableBodyContentForRow(i));
                        } catch (TableRowDoesNotExistException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return tableContentWithinAllRows;
    }

}
