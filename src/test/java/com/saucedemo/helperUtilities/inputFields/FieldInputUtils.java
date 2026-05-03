package com.saucedemo.helperUtilities.inputFields;

import com.saucedemo.enums.WaitStrategy;
import com.saucedemo.factories.ExplicitWaitFactory;
import com.saucedemo.helperUtilities.emailGen.EmailGeneratorUtils;
import com.saucedemo.helperUtilities.number_StringGen.NumberGeneratorUtils;
import com.saucedemo.helperUtilities.number_StringGen.TextGeneratorUtils;
import org.openqa.selenium.WebElement;

public class FieldInputUtils {

    public static void inputFieldValueBasedOnValueType(String valueType, int numberOfChars, WebElement element, String caseType) {
        if (element != null) {
            WebElement waited = ExplicitWaitFactory.performExplicitWait(WaitStrategy.VISIBLE, element);
            if (waited == null) waited = element;
            waited.clear();
            if (valueType.equals("Number")) {
                waited.sendKeys(NumberGeneratorUtils.generateNumberWithLength(numberOfChars));
            } else if (valueType.equals("String")) {
                waited.sendKeys(TextGeneratorUtils.generateStringWithLengthAndCase(numberOfChars, caseType));
            } else if (valueType.equals("Numeric Email")) {
                waited.sendKeys(EmailGeneratorUtils.generateEmailWithNumbersAndLength(numberOfChars));
            } else if (valueType.equals("Textual Email")) {
                waited.sendKeys(EmailGeneratorUtils.generateEmailWithTextAndLengthLessThanFiveChars(numberOfChars, caseType));
            } else if (valueType.equals("Textual Email with < 5 chars")) {
                waited.sendKeys(EmailGeneratorUtils.generateEmailWithTextAndLengthLessThanFiveChars(numberOfChars, caseType));
            } else if (valueType.equals("Numeric Email with < 5 chars")) {
                waited.sendKeys(EmailGeneratorUtils.generateEmailWithNumbersAndLengthLessThanFiveChars(numberOfChars));
            }
        }
    }
}
