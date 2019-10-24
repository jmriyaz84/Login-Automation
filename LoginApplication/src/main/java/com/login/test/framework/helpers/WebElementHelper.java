/*
 * Copyright (c) 2019 The Emirates Group.
 * All Rights Reserved.
 *
 * The information specified here is confidential and remains property of the Emirates Group.
 */
package com.login.test.framework.helpers;

import com.login.test.pages.PageFactory;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import static org.openqa.selenium.Keys.TAB;

/**
 * Created by S746032 on 23/11/2015.
 */
public class WebElementHelper {

    public static boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    public static void enterText(WebElement element, String value) throws Exception {
        int count = 0;
        do {
            try {
                clickElement(element);
                element.clear();
                element.sendKeys(value);
                count++;
            } catch (Exception ex) {
                count++;
            }
        } while (element.getAttribute("value") != null &&
                !element.getAttribute("value").equalsIgnoreCase(value)
                && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
        if (element.getAttribute("value") != null && !element.getAttribute("value").equalsIgnoreCase(value)) {
            throw new Exception("Entered text : " + element.getAttribute("value")
                    + " doesnt match with given input text : " + value);
        }
        element.sendKeys(TAB);
        WaitHelper.waitFor(10);
    }

    public static void enterTextInSequence(WebElement element, String value, boolean tab) throws Exception {
        int count = 0;
        do {
            try {
                clickElement(element);
                element.clear();
                for (int i = 0; i < value.length(); i++) {
                    element.sendKeys(value.substring(i, i + 1));
                    WaitHelper.waitFor(100);
                }
                count++;
            } catch (Exception ex) {
                count++;
            }
        } while (!element.getAttribute("value").equalsIgnoreCase(value)
                && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
        if (!element.getAttribute("value").equalsIgnoreCase(value)) {
            throw new Exception("Entered text : " + element.getAttribute("value")
                    + " doesnt match with given input text : " + value);
        }
        if (tab) {
            element.sendKeys(TAB);
        }
    }

    public static void enterTextWithOutTab(WebElement element, String value) throws Exception {
        int count = 0;
        do {
            try {
                clickElement(element);
                element.clear();
                element.sendKeys(value);
                count++;
            } catch (Exception ex) {
                count++;
            }
        } while (!element.getAttribute("value").equalsIgnoreCase(value)
                && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
        if (!element.getAttribute("value").equalsIgnoreCase(value)) {
            throw new Exception("Entered text : " + element.getAttribute("value")
                    + " doesnt match with given input text : " + value);
        }
    }

    public static void clickElement(WebElement element) throws Exception {
        int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
        long time = 1000 * seconds;
        boolean timeout = false;
        while (!timeout && time > 0) {
            try {
                moveFocusOnElement(element);
                element.click();
                Thread.sleep(100);
                timeout = true;
            } catch (Exception ex) {
                timeout = false;
                Thread.sleep(100);
                time = time - 100;
            }
        }
        if (!timeout) {
            throw new Exception("Element not clickable at the moment : " + element);
        }
    }

    public static void clickElementNew(WebElement element) throws Exception {
        int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
        long time = 1000 * seconds;
        boolean timeout = false;
        while (!timeout && time > 0) {
            try {
//				moveFocusOnElement(element);
//				moveFocusOnElement(element);
                element.click();
                Thread.sleep(100);
                timeout = true;
            } catch (Exception ex) {
                timeout = false;
                Thread.sleep(100);
                time = time - 100;
            }
        }
        if (!timeout) {
            throw new Exception("Element not clickable at the moment : " + element);
        }
    }

    public static String getAttributeValue(WebElement element, String attribute) throws Exception {
        return element.getAttribute(attribute);
    }

    public static String getText(WebElement element) throws Exception {
        int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
        long time = 100 * seconds;
        boolean timeout = false;
        String text = null;
        while (!timeout && time > 0) {
            try {
                text = element.getText().trim();
                if (!StringUtils.isEmpty(text)) {
                    Thread.sleep(100);
                }
                timeout = true;
            } catch (Exception ex) {
                timeout = false;
                time = time / 100;
            }
        }
        if (!timeout) {
            throw new Exception("Failed to retrieve text from the element");
        }
        return text;
    }

    public static boolean isEnabled(WebElement webElement) {
        if (webElement != null) {
            String disabled = webElement.getAttribute("disabled");
            if (disabled == null) {
                disabled = webElement.getAttribute("aria-disabled");
                if (disabled == null) {
                    return true;
                } else {
                    return !disabled.equalsIgnoreCase("true");
                }
            } else {
                return !(disabled.equalsIgnoreCase("disabled") || disabled.equalsIgnoreCase("true"));
            }
        }
        return false;
    }

    public static boolean isHidden(WebElement webElement) {
        if (webElement != null) {
            String hidden = webElement.getAttribute("hidden");
            if (hidden == null) {
                hidden = webElement.getAttribute("aria-hidden");
                if (hidden == null) {
                    return false;
                } else {
                    return hidden.equalsIgnoreCase("true");
                }
            } else {
                return (hidden.equalsIgnoreCase("hidden") || hidden.equalsIgnoreCase("true"));
            }
        }
        return false;
    }

    public static void moveFocusOnElement(WebElement element) {
        try {
            new Actions(PageFactory.getDriver()).moveToElement(element).perform();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public static void doubleClickOnElement(WebElement element) {
        try {
            new Actions(PageFactory.getDriver()).doubleClick(element).perform();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public static boolean isChecked(WebElement webElement) {
        if (webElement != null) {
            String checked = webElement.getAttribute("checked");
            if (checked == null) {
                checked = webElement.getAttribute("aria-checked");
                if (checked == null) {
                    return false;
                } else {
                    return checked.equalsIgnoreCase("true");
                }
            } else {
                return (checked.equalsIgnoreCase("checked") || checked.equalsIgnoreCase("true"));
            }
        }
        return false;
    }

    public static void clickElementUsingJquery(WebElement element) throws Exception {
        try {
            if (element != null) {
                moveFocusOnElement(element);
                JavascriptExecutor js = (JavascriptExecutor) PageFactory.getDriver();
                js.executeScript("arguments[0].click();", element);
            }
        } catch (RuntimeException e) {
            throw new Exception("Could not click on the element: " + element.getClass());
        }
    }


    public static boolean isRequired(WebElement webElement) {
        if (webElement != null) {
            String required = webElement.getAttribute("required");
            if (required == null) {
                required = webElement.getAttribute("aria-required");
                if (required == null) {
                    return false;
                } else {
                    return required.equalsIgnoreCase("true");
                }
            } else {
                return (required.equalsIgnoreCase("required") || required.equalsIgnoreCase("true"));
            }
        }
        return false;
    }

    public static boolean verifyMessage(WebElement element, String message) throws Exception {
        try {
            return (element != null && WebElementHelper.getText(element).
                    contains(message)) ? true : false;
        } catch (Exception e) {
            throw new Exception("failed because of expected message" + message + " shows as -: " + WebElementHelper.getText(element) + e.getMessage());
        }
    }

    public static void selectByVisibleTextFromDropDown(WebElement element, String value) throws Exception {
        int count = 0;
        String selectedValue = "";
        do {
            try {
                Select dropdown = new Select(element);
                dropdown.selectByVisibleText(value);
                selectedValue = dropdown.getFirstSelectedOption().getText();
                count++;
            } catch (Exception ex) {
                count++;
            }

        } while (!element.getAttribute("value").equalsIgnoreCase(value)
                && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
        if (!selectedValue.equalsIgnoreCase(value)) {
            throw new Exception("Selected Value : " + element.getAttribute("value")
                    + " doesnt match with given Value : " + value);
        }
        element.sendKeys(TAB);
        WaitHelper.waitFor(10);
    }

    /**
     * Check if element is read only.
     *
     * @param webElement the web element
     * @return true, if it is read only
     */
    public static boolean isReadOnly(WebElement webElement) {
        return webElement.getAttribute("readOnly").equals("true");
    }
}