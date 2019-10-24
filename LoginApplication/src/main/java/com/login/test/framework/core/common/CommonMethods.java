package com.login.test.framework.core.common;

import com.login.test.framework.helpers.DateHelper;
import com.login.test.framework.helpers.WaitHelper;
import com.login.test.pages.BasePage;
import com.login.test.pages.PageFactory;
import cucumber.api.Scenario;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by S763311 on 10/04/2016.
 */
public class CommonMethods {

	public static final String configFilePath = "src/test/resources/config.properties";
	public static final String testDataFilePath = "src/test/resources/testData.properties";
	public static Properties configFileObject = ReadPropertyFile.loadPropertyFile(configFilePath);
	public static Properties testDataFileObject = ReadPropertyFile.loadPropertyFile(testDataFilePath);
	public static Scenario scenario;

 	public static String getClipBoardContent() throws Exception {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		try {
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			throw new Exception("Could not get copied data from clipboard");
		}

	}

	public static Boolean clickOnElementUsingNameIdentifier(String elementIdentifier) {
		WebElement element = elementExists(elementIdentifier);
		if (element != null) {
			try {
				moveFocusOnElement(element);
				element.click();

			} catch (Exception ex) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}


	public static void fillingTextFieldsOntheScreen() {
		List<WebElement> listOfTextBoxes = PageFactory.getDriver().findElements(By.tagName("input"));
		for (WebElement textbox : listOfTextBoxes) {
			if (!textbox.getAttribute("name").equalsIgnoreCase("brEmailAddress") && textbox.getAttribute("type").equals("text")
					&& textbox.isEnabled()) {

				if (textbox.getAttribute("ssui-numeric-only") != null) {
					if (textbox.getAttribute("ng-model").equals("viewModel.businessDetails.numberOfBusinessTrips")) {
						textbox.sendKeys(randomNumber(1));
					} else {
						textbox.sendKeys(randomNumber(10));
					}
				} else {
					textbox.sendKeys(randomString(8));
				}
			} else if (textbox.getAttribute("type").equals("date") && textbox.isEnabled()) {
				String dateReturned = DateHelper.systemFutureDate(-15000, "dd/mm/yyyy");
				String dateArray[] = dateReturned.split("/");
				for (String eachDateValue : dateArray) {
					textbox.sendKeys(eachDateValue);
				}
			}
		}
	}

	public static void fillingTextFieldsInPersonalDetailsOnSRScreen(int textFieldCount) {
		List<WebElement> listOfTextBoxes = PageFactory.getDriver().findElements(By.tagName("input"));
		int count = 0;
		for (WebElement textbox : listOfTextBoxes) {
			if (count != textFieldCount) {

				if (!textbox.getAttribute("name").equalsIgnoreCase("brEmailAddress")
						&& textbox.getAttribute("type").equals("text") && textbox.isEnabled()) {

					if (textbox.getAttribute("ssui-numeric-only") != null) {
						if (textbox.getAttribute("ng-model")
								.equals("viewModel.businessDetails.numberOfBusinessTrips")) {
							textbox.sendKeys(randomNumber(1));
						} else {
							textbox.sendKeys(randomNumber(10));
						}

					} else {
						textbox.sendKeys(randomString(8));
					}
				} else if (textbox.getAttribute("type").equals("date") && textbox.isEnabled()) {
					String dateReturned = DateHelper.systemFutureDate(-15000, "dd/mm/yyyy");
					String dateArray[] = dateReturned.split("/");
					for (String eachDateValue : dateArray) {
						textbox.sendKeys(eachDateValue);
						textbox.sendKeys(Keys.TAB);
					}
				}
				count++;
			} else {
				break;
			}
		}
	}

	public static void selectingAllDropDownFieldsOntheScreen() {
		List<WebElement> listOfTextBoxes = PageFactory.getDriver().findElements(By.tagName("select"));
		for (WebElement dropDownField : listOfTextBoxes) {
			Select dropDown = new Select(dropDownField);
			if (!dropDown.getOptions().isEmpty() && dropDownField.isEnabled()) {
				if (dropDownField.getAttribute("name").equalsIgnoreCase("country")) {
					dropDown.selectByIndex(20);
				} else {
					dropDown.selectByIndex(1);
				}
			}
		}
	}

	public static void selectingAllDropDownFieldsOnSRScreen(int noOfDropDownFields) {
		List<WebElement> listOfTextBoxes = PageFactory.getDriver().findElements(By.tagName("select"));
		int count = 0;
		for (WebElement dropDownField : listOfTextBoxes) {
			if (count != noOfDropDownFields) {

				Select dropDown = new Select(dropDownField);
				if (!dropDown.getOptions().isEmpty() && dropDownField.isEnabled()) {
					if (dropDownField.getAttribute("name").equalsIgnoreCase("country")) {
						dropDown.selectByIndex(20);
					} else {
						dropDown.selectByIndex(1);
					}
				}
				count++;
			} else {
				break;
			}

		}
	}

	public static String getCabinClassDesignator(String arg1) {
		if (arg1.equalsIgnoreCase("Business")) {
			return "J";
		} else if (arg1.equalsIgnoreCase("Economy")) {
			return "Y";
		} else if (arg1.equalsIgnoreCase("First")) {
			return "F";
		} else if (arg1.isEmpty()) {
			return "";
		} else {
			return null;
		}
	}

	public static String randomString(int length) {
		return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
	}

	public static String randomAlphabeticString(int length) {
		return RandomStringUtils.randomAlphabetic(length).toLowerCase();
	}

	public static String randomNumber(int length) {
		return RandomStringUtils.randomNumeric(length);
	}

	public static int getCountFromString(String arg1) {
		if (arg1.trim().equals(new String("First")) || arg1.trim().equalsIgnoreCase("one")) {
			return 1;
		} else if (arg1.trim().equals(new String("Second")) || arg1.trim().equalsIgnoreCase("two")) {
			return 2;
		} else if (arg1.trim().equals(new String("Third"))) {
			return 3;
		} else if (arg1.trim().equals(new String("Fourth"))) {
			return 4;
		} else if (arg1.trim().equals(new String("Fifth"))) {
			return 5;
		} else if (arg1.trim().equals(new String("Sixth"))) {
			return 6;
		} else if (arg1.trim().equals(new String("Seventh"))) {
			return 7;
		} else if (arg1.trim().equals(new String("Eighth"))) {
			return 8;
		} else if (arg1.trim().equals(new String("Ninth"))) {
			return 9;
		} else if (arg1.trim().equals(new String("Tenth"))) {
			return 10;
		} else if (arg1.trim().equals(new String("Eleventh"))) {
			return 11;
		} else if (arg1.trim().equals(new String("Twelveth"))) {
			return 12;
		} else if (arg1.trim().equals(new String("Thirteenth"))) {
			return 13;
		} else if (arg1.trim().equals(new String("Fourteenth"))) {
			return 14;
		} else if (arg1.trim().equals(new String("Fifteenth"))) {
			return 15;
		} else if (arg1.trim().equals(new String("Sixteenth"))) {
			return 16;
		} else {
			return 0;
		}

	}

	public static String getCountStringFromNumber(int number) {
		switch (number) {
			case (1):
				return "one";
			case (2):
				return "two";
			case (3):
				return "three";
			case (4):
				return "four";
			case (5):
				return "five";
			case (6):
				return "six";
			case (7):
				return "seven";
			case (8):
				return "eight";
			case (9):
				return "nine";
			case (10):
				return "ten";
			default:
				return null;
		}
	}

	public static String getRowNameFromRowNumber(int rowNumber) {
		switch (rowNumber) {
			case (1):
				return "First";
			case (2):
				return "Second";
			case (3):
				return "Third";
			case (4):
				return "Fourth";
			case (5):
				return "Fifth";
			case (6):
				return "Sixth";
			case (7):
				return "Seventh";
			case (8):
				return "Eighth";
			case (9):
				return "Ninth";
			case (10):
				return "Tenth";
			case (11):
				return "Eleventh";
			case (12):
				return "Twelveth";
			case (13):
				return "Thirteenth";
			case (14):
				return "Fourteenth";
			case (15):
				return "Fifteenth";
			case (16):
				return "Sixteenth";
			default:
				return null;
		}

	}

	public static Boolean clickOnmultiSelectLink(String elementLocator, String elementLocatorMultiSelectButton) {
		WebElement element0 = elementExists(elementLocatorMultiSelectButton);
		if (element0 != null) {
			element0.click();
			WebElement element = elementExists(elementLocator);
			if (element != null) {
				element.click();
				if (!element0.getText().equalsIgnoreCase("Select") && !element0.getText().isEmpty()) {
					element0.click();
					return (true);
				} else {
					return (false);
				}

			} else {
				return (false);
			}
		} else {
			return (false);
		}
	}

	public static int bookingOptionsMapping(String arg1) {
		if (arg1.trim().equals(new String("Redemption"))) {
			return 0;
		} else if (arg1.trim().equals(new String("Staff Travel"))) {
			return 1;
		} else if (arg1.trim().equals(new String("Holidays"))) {
			return 2;
		} else if (arg1.trim().equals(new String("Group"))) {
			return 3;
		} else if (arg1.trim().equals(new String("Non-MITA"))) {
			return 4;
		} else if (arg1.trim().equals(new String("RBD"))) {
			return 5;
		} else {
			return -1;
		}

	}

	public static Boolean checkElementDisplay(String cssClassName) {
		WebElement element2 = elementExists(cssClassName);
		if (element2 != null) {
			if (element2.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static Boolean checkRadioButtonisSelected(String Option) {
		WebElement journeyElement = getJourneyType(Option);
		if (journeyElement != null) {
			return true;
		} else {
			return false;
		}
	}

	public static WebElement getJourneyType(String Option) {
		List<WebElement> divClass = getElementByCSS(testDataFileObject.getProperty("ReturnRadioButtonId"))
				.findElements(By.tagName("div"));
		for (int i = 0; i < divClass.size(); i++) {
			List<WebElement> listOfSubElements = divClass.get(i).findElements(By.tagName("div"));
			for (int j = 0; j < listOfSubElements.size(); j++) {
				if (listOfSubElements.get(j).getText().trim().equals(Option)) {
					return listOfSubElements.get(j);

				}
			}
		}

		return null;
	}

	public static Boolean clickOnElement(String id) {
		WebElement element2 = getElementByName(id);
		if (element2 == null) {
			element2 = getElementByID(id);
			if (element2 == null) {
				element2 = getElementByClassName(id);
				if (element2 == null) {
					element2 = getElementByName(id);
				}
			}
		}
		if (element2 != null) {
			try {
				moveFocusOnElement(id);
				WaitHelper.waitUntilClickable(element2);
				element2.click();
			} catch (Exception ex) {
				element2.click();
			}
			return true;
		} else {
			return false;
		}

	}

	public static void clickElement(WebElement element) throws Exception {
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		boolean timeout = false;
		while (!timeout && time > 0) {
			try {
				new BasePage().moveFocusOnElement(element);
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

	public static Boolean clickOnElement(WebElement element) {
		try {
			clickElement(element);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static Boolean submitElement(String elemnetId) {
		WebElement element2 = elementExists(elemnetId);
		if (element2 != null) {
			element2.submit();
			return true;
		} else {
			return false;
		}

	}

	/**************************
	 * Author: Santosh Metri Created On: 06/06/2015 Comment: Do not use this
	 * method, as it impacts the script performance
	 **************************/

	public static WebElement elementExists(String elementLocator) {
		try {
			WebElement element = PageFactory.getDriver().findElement(By.xpath(elementLocator));
			return element;
		} catch (NoSuchElementException e) {
			try {
				WebElement element = PageFactory.getDriver().findElement(By.id(elementLocator));
				return element;

			} catch (NoSuchElementException e1) {
				try {

					WebElement element = PageFactory.getDriver().findElement(By.cssSelector(elementLocator));
					return element;
				} catch (NoSuchElementException e2) {
					try {

						WebElement element = PageFactory.getDriver().findElement(By.name(elementLocator));
						return element;
					} catch (NoSuchElementException e3) {
						try {
							WebElement element = PageFactory.getDriver().findElement(By.linkText(elementLocator));
							return element;
						} catch (NoSuchElementException e4) {
							try {
								WebElement element = PageFactory.getDriver().findElement(By.className(elementLocator));
								return element;
							} catch (NoSuchElementException e5) {
								try {
									WebElement element = PageFactory.getDriver()
											.findElement(By.partialLinkText(elementLocator));
									return element;
								} catch (NoSuchElementException e6) {
									try {
										WebElement element = PageFactory.getDriver()
												.findElement(By.tagName(elementLocator));
										return element;
									} catch (NoSuchElementException e7) {
										return null;
									}
								}
							}
						}
					}

				}

			}
		}

	}

	public static WebElement elementExistsWithLimit(String elementLocator) {
		try {
			WebElement element = PageFactory.getDriver().findElement(By.id(elementLocator));
			return element;
		} catch (NoSuchElementException e) {
			try {
				WebElement element = PageFactory.getDriver().findElement(By.cssSelector(elementLocator));
				return element;

			} catch (NoSuchElementException e1) {
				try {

					WebElement element = PageFactory.getDriver().findElement(By.name(elementLocator));
					return element;
				} catch (NoSuchElementException e2) {
					return null;

				}

			}
		}

	}

	public static Boolean clearText(String elementLocator) {
		WebElement element = elementExists(elementLocator);
		if (element != null) {
			try {
				element.clear();
			} catch (InvalidElementStateException e) {
				return false;
			}

			return true;
		} else {
			return false;
		}

	}

	public static Boolean enterText(String elementLocator, String text) throws Exception {
		WebElement element = getElementByName(elementLocator);
		if (element == null) {
			element = getElementByID(elementLocator);
			if (element == null)
				element = getElementByCSS(elementLocator);
		}
		if (element != null) {
			WaitHelper.waitUntilClickable(element);
			element.click();
			try {
				element.clear();
			} catch (Exception ex) {
				element.sendKeys(Keys.DELETE);
			}
			element.sendKeys(text);

			element.sendKeys(Keys.TAB);
			return true;
		} else {
			return false;
		}
	}

	public static Boolean enterText(WebElement element, String text) throws Exception {
		if (element != null) {
			WaitHelper.waitUntilClickable(element);
			element.click();
			element.clear();
			element.sendKeys(text);

			element.sendKeys(Keys.TAB);
			return true;
		} else {
			return false;
		}
	}

	public static Boolean enterTextWithKey(String elementLocator, String text, Keys key) throws Exception {
		WebElement element = elementExists(elementLocator);
		if (element != null) {
			WaitHelper.waitUntilClickable(element);
			element.click();
			element.clear();
			element.sendKeys(text);

			if (key != null) {
				element.sendKeys(key);
			}
			return true;
		} else {
			return false;
		}
	}

	public static String getTextFromTextField(String elementLocator) {
		WebElement element = getElementByID(elementLocator);
		if (element == null) {
			element = getElementByName(elementLocator);
		}
		if (element != null) {
			if (PageFactory.getDriver() instanceof JavascriptExecutor) {
				String locator = "'" + elementLocator + "'";
				try {
					String textFieldValue = (String) ((JavascriptExecutor) PageFactory.getDriver())
							.executeScript("return document.getElementsByName(" + locator + ").value;");
					if (textFieldValue == null) {
						String textFieldValue1 = (String) ((JavascriptExecutor) PageFactory.getDriver())
								.executeScript("return document.getElementById(" + locator + ").value;");
						return textFieldValue1;
					} else {
						return textFieldValue;
					}

				} catch (Exception ex) {
					try {
						String textFieldValue = (String) ((JavascriptExecutor) PageFactory.getDriver())
								.executeScript("return document.getElementById(" + locator + ").value;");
						return textFieldValue;
					} catch (Exception e1) {
						return null;
					}

				}
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	public static void setAttributeValue(WebElement element, String value) throws Exception {
		if (PageFactory.getDriver() instanceof JavascriptExecutor) {
			try {
				((JavascriptExecutor) PageFactory.getDriver()).executeScript("arguments[0].value=arguments[1];",
						element, value);
			} catch (Exception ex) {
				throw new Exception("Could not set property value for element : " + element + " and value : " + value +
						" bcoz of : " + ex.getMessage());
			}
		}
	}

	public static String getPassengerCount(String totalNumberOfPax, String passengerType) {
		if (totalNumberOfPax != null && passengerType != null) {
			if (totalNumberOfPax.contains(passengerType)) {
				int indexOf = totalNumberOfPax.indexOf(passengerType);
				String extractedString = totalNumberOfPax.substring(indexOf - 2, indexOf - 1);
				return extractedString;
			} else {
				return "0";
			}
		} else {
			return null;
		}

	}

	public static String systemCurrentDate() {
		SimpleDateFormat formats = new SimpleDateFormat(DateHelper.DEFAULT_DATE_FORMAT);
		return formats.format(new Date());
	}

	public static WebElement getElementByID(String id) {
		try {
			return PageFactory.getDriver().findElement(By.id(id));
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getElementByID(WebElement element, String id) {
		try {
			return element.findElement(By.id(id));
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getElementByLinkText(String linkText) {
		try {
			return PageFactory.getDriver().findElement(By.linkText(linkText));
		} catch (Exception ex) {
			return null;
		}

	}

	public static List<WebElement> getElementsByLinkText(String linkText) {
		try {
			return PageFactory.getDriver().findElements(By.linkText(linkText));
		} catch (Exception ex) {
			return null;
		}

	}

	public static List<WebElement> getElementsByID(String id) {
		try {
			return PageFactory.getDriver().findElements(By.id(id));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> getElementsByID(WebElement element, String id) {
		try {
			return element.findElements(By.id(id));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> getElementsByTagName(String tagName) {
		try {
			return PageFactory.getDriver().findElements(By.tagName(tagName));
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getElementByClassName(String className) {
		try {
			return PageFactory.getDriver().findElement(By.className(className));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> getElementsByClassName(String className) {
		try {
			return PageFactory.getDriver().findElements(By.className(className));
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getElementByCSS(String css) {
		try {
			return PageFactory.getDriver().findElement(By.cssSelector(css));
		} catch (Exception ex) {
			return null;
		}

	}

	public static List<WebElement> getElementsByCSS(String css) {
		try {
			return PageFactory.getDriver().findElements(By.cssSelector(css));
		} catch (Exception ex) {
			return null;
		}
	}

	public static Boolean clickOnHiddenInput(WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) PageFactory.getDriver();
			js.executeScript("arguments[0].click();", element);

			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static String currentDate(String format) {
		SimpleDateFormat formats = new SimpleDateFormat(format);
		return formats.format(new Date());
	}

	public static String systemCurrentDateInvalidFormat() {
		SimpleDateFormat formats = new SimpleDateFormat("dd mmm yyyy");
		return formats.format(new Date());
	}

	public static String systemPastDate(int NumberOfDays, String dateFormat) {
		SimpleDateFormat formats = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -NumberOfDays);
		return formats.format(cal.getTime());
	}

	public static Boolean selectFromDropDown(String elementLocator, int value) throws Exception {
		clickOnElement(testDataFileObject.getProperty("passengerDetails"));
		WebElement element = elementExists(elementLocator);
		if (element != null) {
			Select selectDropDownValue = new Select(element);
			selectDropDownValue.selectByIndex(value);
			return true;
		} else {
			return false;
		}
	}

	public static Boolean selectFromDropDown_Global_BasedOnDropDownValue(String elementLocator, String dropDownOption) {
		WebElement element = elementExists(elementLocator);
		if (element != null) {
			Select dropDownElement = new Select(element);
			try {
				dropDownElement.selectByValue(dropDownOption);
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}

		} else {
			return false;
		}
	}

	public static List<WebElement> retrieveDropDownListValues(String elementLocator) {
		WebElement element = elementExists(elementLocator);
		if (element != null) {
			Select dropDownElement = new Select(element);
			try {
				List<WebElement> listvalues = dropDownElement.getOptions();
				return listvalues;
			} catch (NoSuchElementException e) {
				return null;
			}

		} else {
			return null;
		}
	}

	public static Boolean timeDropDowncheck(WebElement element, String dropDownOptionElementId) {
		for (int i = 1; i < 7; i++) {
			String value = dropDownOptionElementId + i;
			WebElement option = elementExists(testDataFileObject.getProperty(value));
			if (option != null) {
				if (timeDropDownvalueCheck(option.getText())) {
				} else {
					return false;
				}

			} else {
				return false;
			}
		}
		return true;

	}

	public static WebElement getClassOfTravelElement(String elementLocator, String optionValue) {

		WebElement element = getElementByCSS(elementLocator);
		if (element == null) {
			element = getElementByID(elementLocator);
		}
		if (element != null) {
			List<WebElement> listOfElements = element.findElements(By.tagName("ul")).get(0)
					.findElements(By.tagName("li"));

			for (WebElement option : listOfElements) {
				WebElement innerElement = option.findElement(By.tagName("a"));
				if (innerElement.getAttribute("innerHTML").equals(optionValue)) {

					return option;

				}
			}
			return null;
		} else {
			return null;
		}
	}

	public static Boolean classOfTravelDropDown(String dropDownOptionElementId) throws Exception {
		WebElement option = elementExists(dropDownOptionElementId);
		if (option != null) {
			try {
				WaitHelper.waitUntilClickable(option);
				option.click();
				return true;
			} catch (ElementNotVisibleException e) {
				return false;
			}
		} else {
			return false;
		}

	}

	public static Boolean timeDropDownvalueCheck(String text) {
		if (text.trim().equals(new String("Any time").trim())) {
			return true;
		} else if (text.trim().equals(new String("A.M.").trim())) {
			return true;
		} else if (text.trim().equals(new String("P.M.").trim())) {
			return true;
		} else if (text.trim().equals(new String("Morning").trim())) {
			return true;
		} else if (text.trim().equals(new String("Evening").trim())) {
			return true;
		} else if (text.trim().equals(new String("Afternoon").trim())) {
			return true;
		} else if (text.trim().equals(new String("Night").trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean airlineFieldNonEditableCheck(String elementId) {
		Assert.assertTrue(checkElementDisplay(testDataFileObject.getProperty(elementId)));
		WebElement airlineField = elementExists(testDataFileObject.getProperty(elementId));
		if (!airlineField.getAttribute("disabled").equals(new String("true"))) {
			return (false);
		} else {
			return (true);
		}
	}

	public static Scenario getScenario() {
		return scenario;
	}


	public static List<WebElement> getWebElements(String elementLocator) {
		try {
			List<WebElement> listofElements = PageFactory.getDriver().findElements(By.xpath(elementLocator));
			if (listofElements.isEmpty()) {
				throw new NoSuchElementException("element not found");
			}
			return listofElements;
		} catch (NoSuchElementException e) {
			try {
				List<WebElement> listofElements = PageFactory.getDriver().findElements(By.id(elementLocator));

				if (listofElements.isEmpty()) {
					throw new NoSuchElementException("element not found");
				}
				return listofElements;

			} catch (NoSuchElementException e1) {
				try {

					List<WebElement> listofElements = PageFactory.getDriver()
							.findElements(By.className(elementLocator));
					if (listofElements.isEmpty()) {
						throw new NoSuchElementException("element not found");
					}
					return listofElements;
				} catch (NoSuchElementException e2) {
					try {
						List<WebElement> listofElements = PageFactory.getDriver()
								.findElements(By.cssSelector(elementLocator));
						if (listofElements.isEmpty()) {
							throw new NoSuchElementException("element not found");
						}
						return listofElements;
					} catch (NoSuchElementException e3) {
						try {
							List<WebElement> listofElements = PageFactory.getDriver()
									.findElements(By.linkText(elementLocator));
							if (listofElements.isEmpty()) {
								throw new NoSuchElementException("element not found");
							}
							return listofElements;
						} catch (NoSuchElementException e4) {
							try {
								List<WebElement> listofElements = PageFactory.getDriver()
										.findElements(By.name(elementLocator));
								if (listofElements.isEmpty()) {
									throw new NoSuchElementException("element not found");
								}
								return listofElements;
							} catch (NoSuchElementException e5) {
								try {
									List<WebElement> listofElements = PageFactory.getDriver()
											.findElements(By.partialLinkText(elementLocator));
									if (listofElements.isEmpty()) {
										throw new NoSuchElementException("element not found");
									}
									return listofElements;
								} catch (NoSuchElementException e6) {
									try {
										List<WebElement> listofElements = PageFactory.getDriver()
												.findElements(By.tagName(elementLocator));
										if (listofElements.isEmpty()) {
											throw new NoSuchElementException("element not found");
										}
										return listofElements;
									} catch (NoSuchElementException e7) {
										e7.printStackTrace();
										return null;
									}
								}
							}
						}
					}

				}

			}
		}
	}

	public static List<WebElement> getElementByTagName(String tagName) {

		try {
			return PageFactory.getDriver().findElements(By.tagName(tagName));
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getElementByName(String nameLocator) {
		try {
			return PageFactory.getDriver().findElement(By.name(nameLocator));
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getElementByName(WebElement element, String nameLocator) {
		try {
			return element.findElement(By.name(nameLocator));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> getElementsByName(String nameLocator) {
		try {
			return PageFactory.getDriver().findElements(By.name(nameLocator));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> getElementsByName(WebElement element, String nameLocator) {
		try {
			return element.findElements(By.name(nameLocator));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> elementExistsByName(String elementLocator) {

		try {
			return PageFactory.getDriver().findElements(By.name(elementLocator));
		} catch (Exception ex) {
			return null;
		}
	}

	public static List<WebElement> getListOfWebElements(String elementLocator) {
		List<WebElement> listOfWebElements = getWebElements(elementLocator);
		if (listOfWebElements != null) {
			return listOfWebElements;

		} else {
			return null;
		}

	}

	public static List<WebElement> getListOfEyeElements(String arg1) {

		WebElement tableObject = elementExists(testDataFileObject.getProperty("tableName_flexiGrid"));
		List<WebElement> listOfEyes = tableObject.findElement(By.tagName("thead")).findElements(By.tagName("th"));

		if (listOfEyes != null) {
			List<WebElement> eyeElement = new ArrayList<WebElement>();
			if (arg1.equals("OpenEye")) {
				for (int i = 1; i < listOfEyes.size(); i++) {
					try {
						eyeElement.add(listOfEyes.get(i)
								.findElement(By.cssSelector(testDataFileObject.getProperty("EyeOpen_Class"))));
					} catch (Exception ex) {
						ex.getMessage();
					}
				}
			} else if (arg1.equals("ClosedEye")) {
				for (int i = 1; i < listOfEyes.size(); i++) {
					try {
						eyeElement.add(listOfEyes.get(i)
								.findElement(By.cssSelector(testDataFileObject.getProperty("EyeClose_Class"))));
					} catch (Exception ex) {
						ex.getMessage();
					}
				}
			}
			return eyeElement;
		} else {
			return null;
		}

	}

	public static int getElementCount(String elementLocator) {
		List<WebElement> listOfWebElements = getWebElements(elementLocator);
		if (listOfWebElements != null) {
			return listOfWebElements.size();

		} else {
			return -1;
		}
	}

	public static Boolean checkSortingInAlphabeticalOrder(List<WebElement> listOfWebElements) {
		for (int i = 3; i < listOfWebElements.size(); i++) {
			for (int j = i + 1; j < listOfWebElements.size(); j++) {
				if (listOfWebElements.get(i).getText().compareTo(listOfWebElements.get(j).getText()) == 0) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static Boolean predictiveSearchResultSortingInAlphabeticalOrder(List<WebElement> listOfWebElements) {
		for (int i = 0; i < listOfWebElements.size(); i++) {
			for (int j = i + 1; j < listOfWebElements.size(); j++) {
				if (listOfWebElements.get(i).getText().compareTo(listOfWebElements.get(j).getText()) == 0) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static Boolean checkPredictiveSearchList(List<WebElement> listOfWebElemets, String staticProperty) {
		if (listOfWebElemets != null) {
			for (int i = 0; i < listOfWebElemets.size(); i++) {
				String optionResult = listOfWebElemets.get(i)
						.findElement(By.tagName(testDataFileObject.getProperty("predictiveSearchTagName"))).getText();
				String staticOption = testDataFileObject.getProperty(staticProperty + i);
				String leavingFromField = getTextFromTextField(testDataFileObject.getProperty("LeavingFrom"));

				if (leavingFromField.length() <= 3) {
					Assert.assertEquals(optionResult, staticOption);
				} else {
					String leavingFromFieldWithAirportCode = leavingFromField.substring(0, 3);
					if (!optionResult.contains(leavingFromFieldWithAirportCode)) {
						Assert.assertEquals(optionResult, staticOption);
					} else {
						return false;
					}

				}

			}
			return true;
		} else {
			return false;
		}
	}


	public static boolean isValidDate(String searchDate, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			Date date = dateFormat.parse(searchDate);
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			if (date.compareTo(cal.getTime()) >= 0) {
				if (!searchDate.substring(5).equals("2016")) {
					return true;
				} else {
					return false;
				}
			}
		} catch (ParseException pe) {
			pe.getMessage();
		}
		return false;
	}

	public static boolean isEnabled(String element) {
		WebElement webElement = elementExists(element);
		if (webElement != null) {
			String disabled = webElement.getAttribute("disabled");
			if (disabled == null || !disabled.equals("true")) {
				disabled = webElement.getAttribute("aria-disabled");
				if (disabled == null || !disabled.equals("true")) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkPredictiveSearch(String elementLocator, String text, int index) {
		List<WebElement> listOfWebElements = getListOfWebElements(elementLocator);
		if (listOfWebElements != null) {
			for (int i = 0; i < listOfWebElements.size(); i++) {
				String optionResult = listOfWebElements.get(i).findElement(By.tagName("a")).getText();
				Assert.assertTrue(optionResult.toLowerCase().split(",")[index].indexOf(text.toLowerCase()) >= 0);
			}
			return true;
		} else {
			return false;
		}
	}

	public static Boolean clickOnElement(String id, int index) {
		WebElement element2 = getWebElements(id).get(index);
		if (element2 != null) {
			element2.click();
			return true;
		} else {
			return false;
		}
	}

	public static Boolean enterText(String inputElementLocator, String elementLocator, String dropDownList, int index,
									String text) throws Exception {
		WebElement element = elementExists(elementLocator);
		WebElement inputElement = getWebElements(inputElementLocator).get(index);
		if (element != null) {
			WaitHelper.waitUntilClickable(element);
			element.click();
			inputElement.sendKeys(text);

			inputElement.sendKeys(Keys.TAB);
			return true;
		} else {
			return false;
		}
	}

	public static boolean selectDropDownMatch(String listElement) {
		List<WebElement> list = getWebElements(listElement);

		if (list != null) {
			for (WebElement e : list) {
				if (e.getText().trim() != null) {
					e.click();
					return true;
				}
			}
		}
		return false;
	}

	public static boolean matchFromList(String clickLoc, String valLocator, String val) {
		List<WebElement> list = getWebElements(valLocator);
		if (list != null) {
			for (WebElement e : list) {
				if (e.getText().indexOf(val) >= 0)
					return true;
			}
		}
		return false;
	}

	public static Boolean selectFromDropDownBasedOnDropDownValue(WebElement element, String dropDownOption) {
		if (element != null) {
			Select dropDownElement = new Select(element);
			try {
				dropDownElement.selectByValue(dropDownOption);
				element.sendKeys(Keys.TAB);
				return true;
			} catch (NoSuchElementException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static WebElement getParentNode(WebElement element) {
		WebDriver driver = ((WrapsDriver) element).getWrappedDriver();
		WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].parentNode;",
				element);
		return parent;
	}

	public static int getRandomNumber(int low, int high) {
		Random r = new Random();
		return (r.nextInt(high - low) + low);

	}

	public static Boolean clickOnElement(WebElement element, String id) throws Exception {
		WebElement element2 = element.findElement(By.id(id));
		if (element2 != null) {
			try {
				moveFocusOnElement(id);
				WaitHelper.waitUntilClickable(element2);
				element2.click();
			} catch (Exception ex) {
				element2.click();
			}
			return true;
		} else {
			return false;
		}

	}

	public static Boolean clickOnElementUseCssId(WebElement element, String cssid) throws Exception {
		WebElement element2 = null;
		if (element != null) {
			element2 = element.findElement(By.cssSelector(cssid));
		} else {
			element2 = getElementByCSS(cssid);
		}
		if (element2 != null) {
			try {
				WaitHelper.waitUntilClickable(element2);
				element2.click();
			} catch (Exception ex) {
				return false;
			}
			return true;
		} else {
			return false;
		}

	}

	public static boolean isElementClickable(WebElement element, String cssid) throws Exception {

		WebElement element2 = null;
		if (element != null) {
			element2 = element.findElement(By.cssSelector(cssid));
		} else {
			element2 = getElementByCSS(cssid);
		}
		if (element2 != null) {
			String previous = PageFactory.getDriver().getPageSource();
			moveFocusOnElement(cssid);
			try {
				element2.click();
			} catch (Exception ex) {
				return false;
			}
			String current = PageFactory.getDriver().getPageSource();
			return !previous.equals(current);
		} else {
			return false;
		}
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

	public static boolean isElementDisabled(WebElement element) {
		return element.getAttribute("disabled") != null;
	}

	public static void moveFocusOnElement(String id) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) PageFactory.getDriver();
			jse.executeScript("document.getElementById('" + id + "').focus();");
		} catch (Exception ex) {
			try {
				JavascriptExecutor jse = (JavascriptExecutor) PageFactory.getDriver();
				jse.executeScript("document.getElementsByName('" + id + "')[0].focus();");
			} catch (Exception e1) {
				try {
					JavascriptExecutor jse = (JavascriptExecutor) PageFactory.getDriver();
					jse.executeScript("document.getElementsByClassName('" + id + "')[0].focus();");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public static void moveFocusOnElement(WebElement element) {
		try {
			new Actions(PageFactory.getDriver()).moveToElement(element).perform();
		} catch (Exception ex) {

		}
	}

	public static boolean isEnabled(WebElement webElement) {
		if (webElement != null) {
			String disabled = webElement.getAttribute("disabled");
			if (disabled == null || !disabled.equals("true")) {
				disabled = webElement.getAttribute("aria-disabled");
				if (disabled == null || !disabled.equals("true")) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isElementClickable(WebElement element) throws Exception {
		if (element != null) {
			try {
				String previous = PageFactory.getDriver().getPageSource();
				element.click();
				String now = PageFactory.getDriver().getPageSource();
				return !previous.equals(now);
			} catch (Exception ex) {
				return false;
			}

		} else {
			return false;
		}
	}

	public static Boolean waitForElementToBeDisplayedByCss(String cssSelector) {
		WebDriverWait wait = new WebDriverWait(PageFactory.getDriver(), 20);
		WebElement elementExsist = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
		if (elementExsist != null) {
			return (true);
		} else {
			return (false);
		}
	}

	public static Boolean waitForElementToBeDisplayedById(String elementLocator) {
		WebDriverWait wait = new WebDriverWait(PageFactory.getDriver(), 20);
		WebElement elementExsist = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementLocator)));
		if (elementExsist != null) {
			return (true);
		} else {
			return (false);
		}
	}

	public static List<WebElement> getElementsByTagName(WebElement element, String tagName) throws Exception {
		try {
			List<WebElement> element2 = null;
			if (element != null) {
				element2 = element.findElements(By.tagName(tagName));
			} else {
				element2 = getElementByTagName(tagName);
			}
			return element2;
		} catch (Exception ex) {
			return null;
		}
	}

	public static WebElement getOnlyElementByTagName(String tagName) {
		try {
			return PageFactory.getDriver().findElement(By.tagName(tagName));
		} catch (Exception ex) {
			return null;
		}

	}

	public static WebElement getOnlyElementByTagName(WebElement element, String tagName) {
		try {
			WebElement element2 = null;
			if (element != null) {
				element2 = element.findElement(By.tagName(tagName));
			} else {
				element2 = getOnlyElementByTagName(tagName);
			}
			return element2;
		} catch (Exception ex) {
			return null;
		}

	}

	public static List<WebElement> getElementsByCSS(WebElement element, String css) throws Exception {
		try {
			List<WebElement> element2 = null;
			if (element != null) {
				element2 = element.findElements(By.cssSelector(css));
			} else {
				element2 = getElementsByCSS(css);
			}
			return element2;
		} catch (Exception ex) {
			ex.getMessage();
			return null;
		}
	}

	public static WebElement getElementByCSS(WebElement element, String css) throws Exception {
		try {
			WebElement element2 = null;
			if (element != null) {
				element2 = element.findElement(By.cssSelector(css));
			} else {
				element2 = getElementByCSS(css);
			}
			return element2;
		} catch (Exception ex) {
			return null;
		}
	}

	public static long getDayCount(Date dateStart, Date dateEnd) {
		long diff = -1;
		try {
			diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
		} catch (Exception ex) {
		}
		return diff;
	}

	public static boolean findBackDrop() throws Exception {
		try {
			WebElement backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_menu_class_one"));
			if (backdrop != null) {
				return true;
			} else {
				backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_menu_class_two"));
				if (backdrop != null) {
					return true;
				} else {
					backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_menu_class_one"));
					if (backdrop != null) {
						return true;
					} else {
						backdrop = getOnlyElementByTagName("md-backdrop");
						if (backdrop != null) {
							return true;
						}
					}
				}
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	public static void closeMenuBackDrop() throws Exception {
		try {
			int count = 0;
			do {
				removeMenuBackDrop();
				count++;
			} while (findBackDrop() && count <= 2);
		} catch (Exception ex) {
			removeMenuBackDrop();
		}
	}

	public static void removeMenuBackDrop() throws Exception {
		try {
			WebElement backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_menu_class_one"));
			if (backdrop != null) {
				backdrop.click();
			} else {
				backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_menu_class_two"));
				if (backdrop != null) {
					backdrop.click();
				} else {
					backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_menu_class_one"));
					if (backdrop != null) {
						backdrop.click();
					} else {
						backdrop = getOnlyElementByTagName("md-backdrop");
						if (backdrop != null) {
							backdrop.click();
						}
					}
				}
			}
		} catch (Exception ex) {
		}
	}

	public static void removeBackDropScroll() {
		try {
			WebElement backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_scroll_class"));
			if (backdrop != null) {
				backdrop.click();
			} else {
				backdrop = getOnlyElementByTagName("md-backdrop");
				if (backdrop != null) {
					backdrop.click();
				}
			}
		} catch (Exception ex) {
		}
	}

	public static void removeSelectBackDrop() throws Exception {
		try {
			WebElement backdrop = getElementByCSS(testDataFileObject.getProperty("backdrop_select_class"));
			if (backdrop != null) {
				backdrop.click();
			} else {
				backdrop = getOnlyElementByTagName("md-backdrop");
				if (backdrop != null) {
					backdrop.click();
				}
			}
		} catch (Exception ex) {
		}
	}

	public static boolean isElementSelectedByName(String name) throws Exception {
		String nameInLowercase = null;
		WebElement element = null;
		String elementValue = null;
		try {
			if (name != null) {
				nameInLowercase = name.toLowerCase();
				element = getElementByName(nameInLowercase);
			}
			elementValue = ((element) != null) ? element.getAttribute("aria-checked") : null;
		} catch (Exception ex) {
			throw new Exception("Error while selecting element ");
		}
		return "true".equals(elementValue);

	}

	public static WebElement getElement(String identifier, String type) throws Exception {
		WebElement element = null;
		if (type.toLowerCase().equals("id")) {
			element = getElementByID(identifier);
		} else if (type.toLowerCase().equals("css")) {
			element = getElementByCSS(identifier);
		} else if (type.toLowerCase().equals("name")) {
			element = getElementByName(identifier);
		} else if (type.toLowerCase().equals("class")) {
			element = getElementByClassName(identifier);
		} else if (type.toLowerCase().equals("linktext")) {
			element = getElementByLinkText(identifier);
		} else if (type.toLowerCase().equals("tagname")) {
			element = getOnlyElementByTagName(identifier);
		}
		return element;
	}

	public static List<WebElement> getElements(String identifier, String type) throws Exception {
		List<WebElement> element = null;
		if (type.toLowerCase().equals("id")) {
			element = getElementsByID(identifier);
		} else if (type.toLowerCase().equals("css")) {
			element = getElementsByCSS(identifier);
		} else if (type.toLowerCase().equals("name")) {
			element = getElementsByName(identifier);
		} else if (type.toLowerCase().equals("class")) {
			element = getElementsByClassName(identifier);
		} else if (type.toLowerCase().equals("linktext")) {
			element = getElementsByLinkText(identifier);
		} else if (type.toLowerCase().equals("tagname")) {
			element = getElementByTagName(identifier);
		}
		return element;
	}

	public static WebElement getElement(WebElement parent, String identifier, String type) throws Exception {
		WebElement element = null;
		if (type.toLowerCase().equals("id")) {
			element = getElementByID(parent, identifier);
		} else if (type.toLowerCase().equals("css")) {
			element = getElementByCSS(parent, identifier);
		} else if (type.toLowerCase().equals("name")) {
			element = getElementByName(parent, identifier);
		} else if (type.toLowerCase().equals("class")) {
			element = getElementByClassName(identifier);
		} else if (type.toLowerCase().equals("linktext")) {
			element = getElementByLinkText(identifier);
		} else if (type.toLowerCase().equals("tagname")) {
			element = getOnlyElementByTagName(parent, identifier);
		}

		return element;
	}

	public static List<WebElement> getElements(WebElement parent, String identifier, String type) throws Exception {
		List<WebElement> element = null;
		if (type.toLowerCase().equals("id")) {
			element = getElementsByID(parent, identifier);
		} else if (type.toLowerCase().equals("css")) {
			element = getElementsByCSS(parent, identifier);
		} else if (type.toLowerCase().equals("name")) {
			element = getElementsByName(parent, identifier);
		} else if (type.toLowerCase().equals("class")) {
			element = getElementsByClassName(identifier);
		} else if (type.toLowerCase().equals("linktext")) {
			element = getElementsByLinkText(identifier);
		} else if (type.toLowerCase().equals("tagname")) {
			element = getElementsByTagName(parent, identifier);
		}

		return element;
	}

}