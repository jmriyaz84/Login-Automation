/*
 * Copyright (c) 2019 The Emirates Group.
 * All Rights Reserved.
 * 
 * The information specified here is confidential and remains property of the Emirates Group.
 */
package com.login.test.pages;

import com.login.test.framework.core.common.CommonMethods;
import com.login.test.framework.helpers.DateHelper;
import com.login.test.framework.helpers.PropertyHelper;
import com.login.test.framework.helpers.WaitHelper;
import com.login.test.framework.helpers.WebElementHelper;
import com.login.test.framework.hooks.BrowserManagement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by S717259
 */
public class BasePage {

	private static final String BASE_PATH = "src/main/java/";
	public Properties properties;

	public BasePage() {
		try {
			String filename = this.getClass().getName().replaceAll("\\.", "/") + ".properties";
			properties = new Properties();
			FileInputStream input = new FileInputStream(BASE_PATH + filename);
			properties.load(input);
			String baseFileName = BasePage.class.getName().replaceAll("\\.", "/") + ".properties";
			FileInputStream baseInput = new FileInputStream(BASE_PATH + baseFileName);
			properties.load(baseInput);
		} catch (IOException ioe) {
			ioe.getMessage();
		}
	}

	public String getProperty(String name) {
		return properties.getProperty(name);
	}

	public boolean verifyPage(String elementName) throws Exception {
		int counter = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		while (counter > 0) {
			try {
				getElementWithoutWait(elementName);
				return true;
			} catch (Exception ex) {
				WaitHelper.waitFor(100);
				counter = counter - 1;
			}
		}
		throw new Exception("Page not loaded.........with element : " + elementName);
	}

	public void loadPage() throws Exception {
		int count = 0;
		boolean loaded = false;
		do{
			try {
				//loaded = PageFactory.getQuickSearchPage().checkQuickSearchScreenLoaded();
			} catch (Exception ex) {
				System.out.println("Loading quick search took more time due to : "+ex.getMessage());
				BrowserManagement.closeBrowser();
				BrowserManagement.prepareBrowser();
			}
			count++;
		}while(!loaded && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
	}

	public void loadAdvancePage(String url) throws Exception {
		try {
			loadPage();
			PageFactory.getDriver().navigate().to(PropertyHelper.getBaseUrl() + url);
		} catch (Exception ex) {
			throw new Exception("Page could not be loaded due to : " + ex.getMessage());
		}
	}

	public void clickElement(String element, boolean disappear) throws Exception {
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		boolean timeout = false;
		while (!timeout && time > 0) {
			try {
				Thread.sleep(100);
				getElementWithoutWait(element).click();
				Thread.sleep(100);
				if (disappear && isElementStillPresent(element)) {
					throw new Exception("Will try to click again");
				}
				timeout = true;
			} catch (Exception ex) {
				if (disappear && !isElementStillPresent(element)) {
					timeout = true;
				} else {
					timeout = false;
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception("Element not clickable at the moment : " + element);
		}
		WaitHelper.waitFor(1000);
	}

	public boolean isElementStillPresent(String element) {
		try {
			WebElement present = getWebElement(element, false);
			return (present != null);
		} catch (Exception ex) {
			return false;
		}
	}

	public WebElement getElement(String name) throws Exception {
		String[] property = getElementProperty(name);
		WebElement element;
		try {
			element = getWebElement(property[0], property[1], true);
			if (element == null) {
				throw new Exception(String.format("Element %s is null : \n", name));
			}
		} catch (Exception ex) {
			throw new Exception("Element is not found in the page : " + name);
		}
		return element;
	}

	public WebElement getElementWithoutWait(String name) throws Exception {
		String[] property = getElementProperty(name);
		WebElement element;
		try {
			element = getWebElement(property[0], property[1], false);
			if (element == null) {
				throw new Exception(String.format("Element %s is null : \n", name));
			}
		} catch (Exception ex) {
			throw new Exception(String.format("Element is not found in the page : \n", name) + ex.getMessage());
		}
		return element;
	}	

	public String[] getElementProperty(String name) throws Exception {
		String element[] = new String[2];
		element[0] = getProperty(name);
		element[1] = getProperty(name + "Type");
		if (element[0] != null && element[1] == null) {
			throw new Exception("Element type cannot be must be provided in the page properties file");
		}
		return element;
	}

	public WebElement getElement(WebElement parent, String name) throws Exception {
		String elementName = getProperty(name);
		String type = getProperty(name + "Type");
		WebElement element;
		if (StringUtils.isBlank(elementName) && StringUtils.isBlank(type)) {
			throw new Exception("Element type cannot be must be provided in the page properties file");
		}
		try {
			element = getWebElement(parent, elementName, type, true);
			if (element == null) {
				throw new Exception(String.format("Element %s is null : \n", name));
			}
		} catch (Exception ex) {
			throw new Exception("Element is not found in the page : " + name);
		}
		return element;
	}

	public WebElement getElement(WebElement parent, String elementName, String type) throws Exception {
		WebElement element;
		if (StringUtils.isBlank(elementName) && StringUtils.isBlank(type)) {
			throw new Exception("Element type cannot be must be provided in the page properties file");
		}
		try {
			element = getWebElement(parent, elementName, type, true);
			if (element == null) {
				throw new Exception("element is null" + elementName);
			}
		} catch (Exception ex) {
			throw new Exception("Element is not found in the page : " + elementName);
		}
		return element;
	}
	

	public WebElement getElement(String elementName, String type) throws Exception {
		WebElement element;
		if (StringUtils.isBlank(elementName) && StringUtils.isBlank(type)) {
			throw new Exception("Element type cannot be must be provided in the page properties file");
		}
		element = getWebElement(elementName, type, true);
		return element;
	}

	public List<WebElement> getElements(String name) throws Exception {
		String elementName = getProperty(name);
		String type = getProperty(name + "Type");
		List<WebElement> elementList = new ArrayList<WebElement>();
		try {
			elementList = getWebElements(elementName, type, true);
			if (elementList.size() == 0) {
				throw new Exception("element list size is 0" + name);
			}
		} catch (Exception ex) {
			throw new Exception(String.format("Element %s is not found in the page : ", name));
		}
		return elementList;
	}

	public List<WebElement> getElements(WebElement parent, String name) throws Exception {
		String[] element = getElementProperty(name);
		List<WebElement> elementList;
		try {
			elementList = getWebElements(parent, element[0], element[1], true);
			if (elementList.size() == 0) {
				throw new Exception("element list size is 0 " + name);
			}
		} catch (Exception ex) {
			throw new Exception(String.format("Element %s is not found in the page : ", name));
		}
		return elementList;
	}

	public WebElement getWebElement(String property, boolean wait) throws Exception {
		String elementName = getProperty(property);
		String type = getProperty(property + "Type");
		WebElement element = null;
		int time = 1000 * WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		boolean timeout = false;
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					element = PageFactory.getDriver().findElement(By.id(elementName));
				} else if (type.equalsIgnoreCase("css")) {
					element = PageFactory.getDriver().findElement(By.cssSelector(elementName));
				} else if (type.equalsIgnoreCase("class")) {
					element = PageFactory.getDriver().findElement(By.className(elementName));
				} else if (type.equalsIgnoreCase("partialLink")) {
					element = PageFactory.getDriver().findElement(By.partialLinkText(elementName));
				} else if (type.equalsIgnoreCase("xpath")) {
					element = PageFactory.getDriver().findElement(By.xpath(elementName));
				} else if (type.equalsIgnoreCase("name")) {
					element = PageFactory.getDriver().findElement(By.name(elementName));
				} else if (type.equalsIgnoreCase("tagname")) {
					element = PageFactory.getDriver().findElement(By.tagName(elementName));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				timeout = true;
			} catch (Exception ex) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception("Element not available at the moment : " + elementName);
		}
		return element;
	}

	public WebElement getWebElement(String elementName, String type, boolean wait) throws Exception {
		WebElement element = null;
		int time = 1000 * WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		boolean timeout = false;
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					element = PageFactory.getDriver().findElement(By.id(elementName));
				} else if (type.equalsIgnoreCase("css")) {
					element = PageFactory.getDriver().findElement(By.cssSelector(elementName));
				} else if (type.equalsIgnoreCase("class")) {
					element = PageFactory.getDriver().findElement(By.className(elementName));
				} else if (type.equalsIgnoreCase("partialLink")) {
					element = PageFactory.getDriver().findElement(By.partialLinkText(elementName));
				} else if (type.equalsIgnoreCase("xpath")) {
					element = PageFactory.getDriver().findElement(By.xpath(elementName));
				} else if (type.equalsIgnoreCase("name")) {
					element = PageFactory.getDriver().findElement(By.name(elementName));
				} else if (type.equalsIgnoreCase("tagname")) {
					element = PageFactory.getDriver().findElement(By.tagName(elementName));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				timeout = true;
			} catch (Exception ex) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception("Element not available at the moment : " + elementName);
		}
		return element;
	}

	public WebElement getWebElement(WebElement parent, String property, boolean wait) throws Exception {
		String elementName = getProperty(property).trim();
		String type = getProperty(property + "Type").trim();
		if (parent == null) {
			return getWebElement(property, wait);
		}
		WebElement element = null;

		int time = 1000 * WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		boolean timeout = false;
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					element = parent.findElement(By.id(elementName));
				} else if (type.equalsIgnoreCase("css")) {
					element = parent.findElement(By.cssSelector(elementName));
				} else if (type.equalsIgnoreCase("class")) {
					element = parent.findElement(By.className(elementName));
				} else if (type.equalsIgnoreCase("partialLink")) {
					element = parent.findElement(By.partialLinkText(elementName));
				} else if (type.equalsIgnoreCase("xpath")) {
					element = parent.findElement(By.xpath(elementName));
				} else if (type.equalsIgnoreCase("name")) {
					element = parent.findElement(By.name(elementName));
				} else if (type.equalsIgnoreCase("tagname")) {
					element = parent.findElement(By.tagName(elementName));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				timeout = true;
			} catch (Exception ex) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception("Element not available at the moment : " + elementName);
		}
		return element;
	}

	public WebElement getWebElement(WebElement parent, String elementName, String type, boolean wait) throws Exception {
		if (parent == null) {
			return getWebElement(elementName, type, wait);
		}
		WebElement element = null;

		int time = 1000 * WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		boolean timeout = false;
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					element = parent.findElement(By.id(elementName));
				} else if (type.equalsIgnoreCase("css")) {
					element = parent.findElement(By.cssSelector(elementName));
				} else if (type.equalsIgnoreCase("class")) {
					element = parent.findElement(By.className(elementName));
				} else if (type.equalsIgnoreCase("partialLink")) {
					element = parent.findElement(By.partialLinkText(elementName));
				} else if (type.equalsIgnoreCase("xpath")) {
					element = parent.findElement(By.xpath(elementName));
				} else if (type.equalsIgnoreCase("name")) {
					element = parent.findElement(By.name(elementName));
				} else if (type.equalsIgnoreCase("tagname")) {
					element = parent.findElement(By.tagName(elementName));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				timeout = true;
			} catch (Exception ex) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception("Element not available at the moment : " + elementName);
		}
		return element;
	}

	public List<WebElement> getWebElements(String property, boolean wait) throws Exception {
		String locator = getProperty(property);
		String type = getProperty(property + "Type");
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		boolean timeout = false;
		List<WebElement> elementList = new ArrayList<WebElement>();
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					elementList = PageFactory.getDriver().findElements(By.id(locator));
				} else if (type.equalsIgnoreCase("css")) {
					elementList = PageFactory.getDriver().findElements(By.cssSelector(locator));
				} else if (type.equalsIgnoreCase("class")) {
					elementList = PageFactory.getDriver().findElements(By.className(locator));
				} else if (type.equalsIgnoreCase("partialLink")) {
					elementList = PageFactory.getDriver().findElements(By.partialLinkText(locator));
				} else if (type.equalsIgnoreCase("xpath")) {
					elementList = PageFactory.getDriver().findElements(By.xpath(locator));
				} else if (type.equalsIgnoreCase("name")) {
					elementList = PageFactory.getDriver().findElements(By.name(locator));
				} else if (type.equalsIgnoreCase("tagname")) {
					elementList = PageFactory.getDriver().findElements(By.tagName(locator));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				if (elementList.isEmpty()) {
					if (!wait) {
						timeout = true;
					} else {
						Thread.sleep(100);
						time = time - 100;
					}
				} else {
					timeout = true;
				}
			} catch (WebDriverException e) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception(String.format("Element %s could not be found in the page : ", locator));
		}
		return elementList;

	}

	public List<WebElement> getWebElements(String locator, String type, boolean wait) throws Exception {
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		boolean timeout = false;
		List<WebElement> elementList = new ArrayList<WebElement>();
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					elementList = PageFactory.getDriver().findElements(By.id(locator));
				} else if (type.equalsIgnoreCase("css")) {
					elementList = PageFactory.getDriver().findElements(By.cssSelector(locator));
				} else if (type.equalsIgnoreCase("class")) {
					elementList = PageFactory.getDriver().findElements(By.className(locator));
				} else if (type.equalsIgnoreCase("partialLink")) {
					elementList = PageFactory.getDriver().findElements(By.partialLinkText(locator));
				} else if (type.equalsIgnoreCase("xpath")) {
					elementList = PageFactory.getDriver().findElements(By.xpath(locator));
				} else if (type.equalsIgnoreCase("name")) {
					elementList = PageFactory.getDriver().findElements(By.name(locator));
				} else if (type.equalsIgnoreCase("tagname")) {
					elementList = PageFactory.getDriver().findElements(By.tagName(locator));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				if (elementList.isEmpty()) {
					if (!wait) {
						timeout = true;
					} else {
						Thread.sleep(100);
						time = time - 100;
					}
				} else {
					timeout = true;
				}
			} catch (WebDriverException e) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception(String.format("Element %s could not be found in the page : ", locator));
		}
		return elementList;

	}

	public List<WebElement> getWebElements(WebElement parent, String property, boolean wait) throws Exception {
		String locator = getProperty(property);
		String type = getProperty(property + "Type");
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		boolean timeout = false;
		List<WebElement> elementList = new ArrayList<WebElement>();
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					elementList = parent.findElements(By.id(locator));
				} else if (type.equalsIgnoreCase("css")) {
					elementList = parent.findElements(By.cssSelector(locator));
				} else if (type.equalsIgnoreCase("class")) {
					elementList = parent.findElements(By.className(locator));
				} else if (type.equalsIgnoreCase("partialLink")) {
					elementList = parent.findElements(By.partialLinkText(locator));
				} else if (type.equalsIgnoreCase("xpath")) {
					elementList = parent.findElements(By.xpath(locator));
				} else if (type.equalsIgnoreCase("name")) {
					elementList = parent.findElements(By.name(locator));
				} else if (type.equalsIgnoreCase("tagname")) {
					elementList = parent.findElements(By.tagName(locator));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				if (elementList.isEmpty()) {
					if (!wait) {
						timeout = true;
					} else {
						Thread.sleep(100);
						time = time - 100;
					}
				} else {
					timeout = true;
				}
			} catch (WebDriverException e) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception(String.format("Element %s could not be found in the page : ", locator));
		}
		return elementList;

	}

	public List<WebElement> getWebElements(WebElement parent, String locator, String type, boolean wait)
			throws Exception {
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		boolean timeout = false;
		List<WebElement> elementList = new ArrayList<WebElement>();
		while (!timeout && time > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					elementList = parent.findElements(By.id(locator));
				} else if (type.equalsIgnoreCase("css")) {
					elementList = parent.findElements(By.cssSelector(locator));
				} else if (type.equalsIgnoreCase("class")) {
					elementList = parent.findElements(By.className(locator));
				} else if (type.equalsIgnoreCase("partialLink")) {
					elementList = parent.findElements(By.partialLinkText(locator));
				} else if (type.equalsIgnoreCase("xpath")) {
					elementList = parent.findElements(By.xpath(locator));
				} else if (type.equalsIgnoreCase("name")) {
					elementList = parent.findElements(By.name(locator));
				} else if (type.equalsIgnoreCase("tagname")) {
					elementList = parent.findElements(By.tagName(locator));
				} else {
					throw new Exception(String.format("Element type %s is not supported at the moment : ", type));
				}
				if (elementList.isEmpty()) {
					if (!wait) {
						timeout = true;
					} else {
						Thread.sleep(100);
						time = time - 100;
					}
				} else {
					timeout = true;
				}
			} catch (WebDriverException e) {
				if (!wait) {
					timeout = true;
				} else {
					Thread.sleep(100);
					time = time - 100;
				}
			}
		}
		if (!timeout) {
			throw new Exception(String.format("Element %s could not be found in the page : ", locator));
		}
		return elementList;

	}

	public void moveFocusOnElement(WebElement element) {
		try {
			new Actions(PageFactory.getDriver()).moveToElement(element).perform();
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	public void removeBackDrop(WebElement elementOnBackdrop) throws Exception {
		try {
			List<WebElement> backDropList = getWebElements("backdrop_tag", false);
			if (backDropList != null && backDropList.size() > 0) {
				for (WebElement backDrop : backDropList) {
					if (WebElementHelper.isEnabled(backDrop) && !WebElementHelper.isHidden(backDrop)) {
						try{
							backDrop.click();
						}catch (Exception ex){
							WebElementHelper.clickElementUsingJquery(backDrop);
						}
					}
				}
			}
			List<WebElement> scrollMaskList = getWebElements("backdrop_scroll_class", false);
			if (scrollMaskList != null && scrollMaskList.size() > 0) {
				for (WebElement scrollMask : scrollMaskList) {
					if (WebElementHelper.isEnabled(scrollMask) && !WebElementHelper.isHidden(scrollMask)) {
						try {
							scrollMask.click();
						} catch (ElementNotVisibleException enve) {
							WaitHelper.waitFor(100);
						} catch (Exception ex) {
							try {
								scrollMask.sendKeys(Keys.ESCAPE);
							} catch (Exception ext) {
								WaitHelper.waitFor(100);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			try{
				if(elementOnBackdrop != null){
					moveFocusOnElement(elementOnBackdrop);
					elementOnBackdrop.sendKeys(Keys.ESCAPE);
				}else{
					new Actions(PageFactory.getDriver()).sendKeys(Keys.ESCAPE).perform();
				}
			}catch (Exception exs){
				System.out.println("Back Drop ESCAPE key failed on element "+elementOnBackdrop+" due to : "+
						exs.getMessage());
			}
		}
	}

	public void waitForPopup(String propertyName) throws Exception{
		try {
			int count = 0;
			WebElement element = null;
			do {
				try {
					WaitHelper.waitFor(1000);
					element = getWebElement(propertyName, false);
				} catch (Exception ex) {
					WaitHelper.waitFor(1);
				}
				count++;
			} while (element == null && count < WaitHelper.WEBELEMENT_POPUP_TIMEOUT);
		} catch (Exception ex) {
			System.out.println(
					"wait until exists failed : more than " + WaitHelper.WEBELEMENT_POPUP_TIMEOUT + " seconds : " +
							"due to :"+ex.getMessage());
		}
	}

	public WebElement getPopupDisplayed(String headerTextToCheck) throws Exception {
		try {
			waitForPopup("popup_window_class");
			WebElement dialogElement = getElement("popup_window_class");
			if (dialogElement != null && dialogElement.getText().toUpperCase().contains(headerTextToCheck.toUpperCase())) {
				WaitHelper.waitFor(1000);
				return dialogElement;
			}
		} catch (Exception ex) {
			throw new Exception("Pop up is not displayed due to : " + ex.getMessage());
		}
		return null;
	}

	public void checkErrorMessageDisplayedOnToast() throws Exception {
		WebElement toastTag = getWebElement("toast_tag", false);
		if(toastTag != null){
			List<WebElement> toastElements = getElements(toastTag, "toast_tag_div");
			for (WebElement toastElement : toastElements) {
				WebElement toastBannerTag = getElement(toastElement, "toast_message");
				WebElement toastBannerIcon = getWebElement(toastElement, "toast_icon", false);
				if(toastBannerIcon != null){
					if(WebElementHelper.getText(toastBannerIcon).trim().toUpperCase().contains("ERROR")){
						throw new Exception("Error message displayed on toast is : " +
								WebElementHelper.getText(toastBannerTag));
					}
				}
			}
		}
	}

	public boolean checkToastBannerDisplayed(String msgToCheck) throws Exception {
		try {
			return getMessageDisplayedOnToast().toUpperCase().contains(msgToCheck.toUpperCase());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	public boolean checkTSTDeletionConfirmationISDisplayed(String msgToCheck) throws Exception {
		try {
			return getMessageOnPopUp().toUpperCase().contains(msgToCheck.toUpperCase());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public String getMessageDisplayedOnToast() throws Exception {
		try {
			WaitHelper.waitUntilExists(this, "toast_tag");
			WebElement toastTag = getElement("toast_tag");
			List<WebElement> toastElements = getElements(toastTag, "toast_tag_div");
			String toastMessage = "";
			for (WebElement toastElement : toastElements) {
				WebElement toastBannerTag = getElement(toastElement, "toast_message");
				toastMessage = toastMessage + WebElementHelper.getText(toastBannerTag);
			}
			return toastMessage;
		} catch (Exception ex) {
			throw new Exception("Could not get message from toast due to : " + ex.getMessage());
		}
	}

	public void closeDefaultPopup() throws Exception {
		waitForPopup("popup_window_class");
		WebElement dialogElement = getWebElement("popup_window_class", false);
		if (dialogElement != null) {
			WebElement dialogToolBarDiv = getWebElement(dialogElement, "popup_actions_class", false);
			List<WebElement> buttons;
			if (dialogToolBarDiv != null) {
				buttons = getWebElements(dialogToolBarDiv, "button", "tagname", true);
				if (buttons != null && buttons.size() > 0) {
					WebElement closeButton = buttons.get(buttons.size() - 2);
					if (WebElementHelper.isHidden(closeButton)) {
						closeButton = buttons.get(buttons.size() - 1);
					}
					WebElementHelper.clickElement(closeButton);
				}
			} else {
				buttons = getWebElements(dialogElement, "button", "tagname", true);
				if (buttons != null && buttons.size() > 0) {
					WebElementHelper.clickElement(buttons.get(buttons.size() - 1));
				}
			}
		}
	}

	public void clickOnOkButtonOnlyIfInfoPopup() throws Exception {
		waitForPopup("popup_window_class");
		WebElement dialogElement = getWebElement("popup_window_class", false);
		if (dialogElement != null) {
			WebElement headerElement = getElement(dialogElement, "popup_window_header_class");
			if (headerElement != null && headerElement.getText().toUpperCase().contains("INFO")) {
				clickOnDefaultPopUpButton(dialogElement, "ok_btn");
			}
		}
	}

	public boolean clickOnOkButtonInAnyInfoPopup(String infoTextToCheck) throws Exception {
		return clickOnPopUpButton("ok_btn", infoTextToCheck);
	}

	public void clickOnNoButtonOnPopUp(String noTextToCheck) throws Exception {
		clickOnPopUpButton("no_btn", noTextToCheck);
	}

	public String getMessageOnPopUp() throws Exception {
		waitForPopup("popup_window_class");
		WebElement dialogElement = getWebElement("popup_window_class", false);
		String message = "";
		if (dialogElement != null) {
			WebElement headerElement = getElement(dialogElement, "popup_window_header_class");
			if (headerElement != null && !headerElement.getText().toUpperCase().contains("ERROR")) {
				message = dialogElement.getText().toUpperCase();
			}
		}
		return message;
	}

	public String getInfoMessageOnPopUp() throws Exception {
		waitForPopup("popup_window_class");
		WebElement dialogElement = getWebElement("popup_window_class", false);
		String message = "";
		if (dialogElement != null) {
			WebElement headerElement = getElement(dialogElement, "popup_window_header_class");
			if (headerElement != null && headerElement.getText().toUpperCase().contains("INFO")) {
				message = dialogElement.getText();
			}
		}
		return message;
	}

	public String getErrorMessageOnPopUp() throws Exception {
		waitForPopup("popup_window_class");
		WebElement dialogElement = getWebElement("popup_window_class", false);
		String message = "";
		if (dialogElement != null) {
			WebElement headerElement = getElement(dialogElement, "popup_window_header_class");
			if (headerElement != null && headerElement.getText().toUpperCase().contains("ERROR")) {
				message = dialogElement.getText().toUpperCase();
			}
		}
		return message;
	}

	public boolean checkInfoMessageOnAnyPopUp(String textToCheck) throws Exception {
		String message = getInfoMessageOnPopUp();
		if (message.isEmpty() || !message.toUpperCase().contains(textToCheck.toUpperCase())) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checkMessageOnAnyPopUp(String textToCheck) throws Exception {
		String message = getMessageOnPopUp();
		if (message.isEmpty() || !message.toUpperCase().contains(textToCheck.toUpperCase())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean clickOnPopUpButton(String btnToClick, String textToCheck) throws Exception {
		waitForPopup("popup_window_class");
		WebElement dialogElement = getWebElement("popup_window_class", false);
		if (dialogElement != null) {
			if (textToCheck == null) {
				clickOnDefaultPopUpButton(dialogElement, btnToClick);
			} else if (dialogElement.getText().toUpperCase().contains(textToCheck.toUpperCase())) {
				clickOnDefaultPopUpButton(dialogElement, btnToClick);
			} else {
				return false;
			}
			return true;
		}
		return false;
	}

	private void clickOnDefaultPopUpButton(WebElement dialogElement, String btn) throws Exception {
		List<WebElement> buttons = getWebElements(dialogElement, "button", "tagname", true);
		boolean continueFlag = false;
		if (buttons != null && buttons.size() > 0) {
			if (btn.equalsIgnoreCase("ok_btn")) {
				for (WebElement element : buttons) {
					try {
						if (element.getAttribute("aria-hidden").equals("true")) {
							continueFlag = true;
						} else {
							continueFlag = false;
						}
					} catch (Exception ex) {
						continueFlag = false;
					}
					String buttonText = element.getText().trim();
					if ((buttonText.equalsIgnoreCase("CREATE") || buttonText.equalsIgnoreCase("YES")
							|| buttonText.equalsIgnoreCase("PROCEED") || buttonText.equalsIgnoreCase("OK")
							|| buttonText.equalsIgnoreCase("DISASSOCIATE") || buttonText.equalsIgnoreCase("ASSOCIATE")
							|| buttonText.equalsIgnoreCase("SUBMIT") || buttonText.equalsIgnoreCase("ISSUE")
							|| buttonText.equalsIgnoreCase("CONTINUE TO PURCHASE")
							|| buttonText.equalsIgnoreCase("END & RETRIEVE BOOKING")
							|| buttonText.equalsIgnoreCase("CONTINUE") || buttonText.equalsIgnoreCase("CREATE TSM")
							|| buttonText.equalsIgnoreCase("RETAIN") || buttonText.equalsIgnoreCase("COPY STRING"))
							|| buttonText.equalsIgnoreCase("BOOK") || buttonText.equalsIgnoreCase("MODIFY")
							|| buttonText.equalsIgnoreCase("BOOK/MODIFY")
							|| buttonText.equalsIgnoreCase("DELETE") || buttonText.equalsIgnoreCase("CLOSE")
							|| buttonText.equalsIgnoreCase("VOID")&& !continueFlag) {
						WebElement checkBox = getWebElement(dialogElement, "md-checkbox", "tagname", false);
						if(checkBox != null){
							if(!WebElementHelper.isChecked(checkBox)){
								WebElementHelper.clickElementUsingJquery(checkBox);
							}
						}
						WebElementHelper.clickElement(element);
						break;
					}

				}
			} else if (btn.equalsIgnoreCase("no_btn")) {
				for (WebElement element : buttons) {
					String buttonText = element.getText().toUpperCase();
					if (buttonText.contains("IGNORE") || buttonText.contains("NO") || buttonText.contains("CANCEL")) {
						WebElementHelper.clickElement(element);
						break;
					}
				}
			}
		}

	}

	public void autoSelectFromDropDown(WebElement element, String code) throws Exception {
		int timeoutCounter = 0;
		if (element != null) {
			if (!StringUtils.isBlank(code)) {
				while (!isAutoSelectValueSelected(element, code)
						&& timeoutCounter < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT) {
					try {
						element.clear();
						element.sendKeys(code);
						WaitHelper.waitFor(2000);
						waitForLinearProgressBarToComplete();
						WaitHelper.waitUntilExists(this, "auto_complete_options_div");
						WaitHelper.waitUntilExists(this, "auto_complete_options");
						List<WebElement> options = getElements("auto_complete_options");
						for(WebElement option : options){
							WebElement optionText = getElement(option, "span", "tagname");
							if(WebElementHelper.getText(optionText).trim().toUpperCase().contains(code.toUpperCase())){
								try{
									WebElementHelper.clickElement(option);
								}catch (Exception ex){
									WebElementHelper.clickElementUsingJquery(option);
								}
								break;
							}
						}
						element.sendKeys(Keys.TAB);
					} catch (Exception ex) {
						System.out.println("Exception came in autoSelectFromDropDown(WebElement element, String code) " +
								"due to : "+ex.getMessage());
						WaitHelper.waitFor(100);
					}
					timeoutCounter++;
				}
			} else {
				element.clear();
				element.sendKeys(Keys.ESCAPE);
			}
			if (!isAutoSelectValueSelected(element, code)) {
				throw new Exception("Code selected is : " + element.getAttribute("value"));
			}
		}
	}

	private boolean isAutoSelectValueSelected(WebElement element, String code) {
		return element.getAttribute("value").trim().toUpperCase().contains(code.toUpperCase())
				&& element.getAttribute("value").trim().length() > 3;
	}

	public void sendCharStream(WebElement element, String inputText) throws Exception {
		char[] arrayOfAirport = inputText.toCharArray();
		for (int i = 0; i < arrayOfAirport.length; i++) {
			element.sendKeys(String.valueOf(arrayOfAirport[i]));
			WaitHelper.waitFor(500);
		}
	}

	private WebElement getOptionsElement(boolean table) throws Exception {
		WebElement optionsDiv = null;
		try {
			if (table) {
				optionsDiv = getElement("select_menu_options_table_div");
			} else {
				optionsDiv = getElement("select_menu_options_div");
			}
		} catch (Exception ex) {
			return null;
		}
		return optionsDiv;
	}

	public WebElement getOptionsElementWithOutWait(boolean table) throws Exception {
		WebElement optionsDiv = null;
		try {
			if (table) {
				optionsDiv = getWebElement("select_menu_options_table_div", false);
			} else {
				optionsDiv = getWebElement("select_menu_options_div", false);
			}
		} catch (Exception ex) {
			return null;
		}
		return optionsDiv;
	}

	private WebElement getMultiOptionsElement(boolean table) throws Exception {
		WebElement optionsDiv = null;
		WaitHelper.waitFor(2000);
		try {
			if (table) {
				optionsDiv = getWebElement("select_menu_options_table_div", false);
			} else {
				optionsDiv = getWebElement("select_menu_options_div", false);
			}
			if (optionsDiv == null) {
				if (table) {
					optionsDiv = getWebElement("multi_select_menu_options_table_div", false);
				} else {
					optionsDiv = getWebElement("multi_select_menu_options_div", false);
				}
			}
		} catch (Exception ex) {
			return null;
		}
		return optionsDiv;
	}

	private void selectSingleOptionName(String option, WebElement optionsDiv) throws Exception {
		List<WebElement> optionsList = null;
		try {
			optionsList = getElements(optionsDiv, "options_tag_name");
			for (WebElement eachElement : optionsList) {
				WebElement optionElement = getElement(eachElement, "options_tag_text");
				if (WebElementHelper.getText(optionElement).trim().toUpperCase().contains(option.toUpperCase())) {
					try {
						WebElementHelper.clickElement(eachElement);
					} catch (Exception ex) {
						try {
							WebElementHelper.clickElementUsingJquery(eachElement);
						} catch (Exception exs) {
							throw new Exception("Error while selecting a single option name " + exs.getMessage());
						}
					}
					break;
				}
			}
		} catch (Exception ex) {
			throw new Exception("Error in selectSingleOptionName() " + ex.getMessage());
		}
	}

	private String selectSingleOptionIndex(Integer optionIndex, WebElement optionsDiv) throws Exception {
		List<WebElement> optionsList = getElements(optionsDiv, "options_tag_name");
		WebElement optionElement = getElement(optionsList.get(optionIndex), "options_tag_text");
		WaitHelper.waitUntilClickable(optionElement);
		WebElementHelper.clickElement(optionElement);
		return optionElement.getText();
	}

	private void selectMultiOptions(List<Integer> options, WebElement optionsDiv) throws Exception {
		List<WebElement> optionsList = getElements(optionsDiv, "options_tag_name");
		if (options == null) {
			for (WebElement optionElement : optionsList) {
				WebElement eachElement = getElement(optionElement, "options_tag_text");
				WaitHelper.waitUntilClickable(eachElement);
				WebElementHelper.clickElement(eachElement);
			}
		} else {
			for (int index = 0; index < options.size(); index++) {
				WebElement eachElement = getElement(optionsList.get(options.get(index)), "options_tag_text");
				WaitHelper.waitUntilClickable(eachElement);
				WebElementHelper.clickElement(eachElement);
			}
		}
	}


	public boolean isSingleDropDownValueSelected(WebElement element, String option) throws Exception {
		String labelSelectedValue = element.getAttribute("aria-label").trim().toUpperCase();
		return (labelSelectedValue.contains(":") && labelSelectedValue.contains(option.toUpperCase()));
	}

	private void clickSelectElement(WebElement element) throws Exception {
		removeBackDrop(element);
		moveFocusOnElement(element);
		WaitHelper.waitUntilClickable(element);
		try {
			WebElementHelper.clickElement(element);
		} catch (Exception ex) {
			WebElementHelper.clickElementUsingJquery(element);
		}
		WaitHelper.waitFor(500);
	}

	private boolean isCodeEntered(WebElement element, String option, boolean table, boolean codeEntered) throws Exception {
		WebElement optionsDiv = getOptionsElement(table);
		if (optionsDiv != null) {
			selectSingleOptionName(option, optionsDiv);
			if (isSingleDropDownValueSelected(element, option)) {
				codeEntered = true;
			}
		}
		return codeEntered;
	}

	private void isOptionSelected(WebElement element, String option) throws Exception {
		removeBackDrop(element);
		if (!isSingleDropDownValueSelected(element, option)) {
			throw new Exception("Option selected is :" + element.getAttribute("aria-label"));
		}
	}

	public void selectSingleOptionFromDropDown(String elementName, String option, boolean table) throws Exception {
		if (option != null && WebElementHelper.isEnabled(getElement(elementName))) {
			moveFocusOnElement(getElement(elementName));
			if (!isSingleDropDownValueSelected(getElement(elementName), option)) {
				int count = 0;
				boolean codeEntered = false;
				do {
					try {
						clickSelectElement(getElement(elementName));
						codeEntered = isCodeEntered(getElement(elementName), option, table, codeEntered);
					} catch (Exception ex) {
						System.out.println("Exception came in selectSingleOptionFromDropDown(String elementName, " +
								"String option, boolean table) due to : "+ex.getMessage());
					}
					count++;
				} while (!codeEntered && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
				isOptionSelected(getElement(elementName), option);
			}
		}
	}


	public void selectSingleOptionFromDropDown(WebElement element, String option, boolean table) throws Exception {
		if (option != null && WebElementHelper.isEnabled(element)) {
			moveFocusOnElement(element);
			if (!isSingleDropDownValueSelected(element, option)) {
				int count = 0;
				boolean codeEntered = false;
				do {
					try {
						clickSelectElement(element);
						WebElement optionsDiv = getOptionsElement(table);
						if (optionsDiv != null) {
							WaitHelper.waitFor(1000);
							selectSingleOptionName(option, optionsDiv);
							if (isSingleDropDownValueSelected(element, option)) {
								codeEntered = true;
							}
						}
					} catch (Exception ex) {
						System.out.println("Exception came in selectSingleOptionFromDropDown(WebElement element, " +
								"String option, boolean table) due to : "+ex.getMessage());
					}
					count++;
				} while (!codeEntered && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
				isOptionSelected(element, option);
			}
		}
	}



	public void selectSingleOptionFromDropDown(WebElement element, Integer optionIndex, boolean table) throws Exception {
		if (optionIndex != null && WebElementHelper.isEnabled(element)) {
			int count = 0;
			boolean codeEntered = false;
			String option = null;
			do {
				try {
					clickSelectElement(element);
					WebElement optionsDiv = getOptionsElement(table);
					if (optionsDiv != null) {
						option = selectSingleOptionIndex(optionIndex, optionsDiv);
						if (option != null && isSingleDropDownValueSelected(element, option)) {
							codeEntered = true;
						}
					}
				} catch (Exception ex) {
					System.out.println("Exception came in selectSingleOptionFromDropDown(WebElement element, " +
							"Integer optionIndex, boolean table) due to : "+ex.getMessage());
				}
				count++;
			} while (!codeEntered && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
			isOptionSelected(element, option);
		}
	}

	public void selectSingleOptionFromDropDown(String elementName, Integer optionIndex, boolean table)
			throws Exception {
		if (optionIndex != null && WebElementHelper.isEnabled(getElement(elementName))) {
			int count = 0;
			boolean codeEntered = false;
			String option = null;
			do {
				try {
					clickSelectElement(getElement(elementName));
					WebElement optionsDiv = getOptionsElement(table);
					if (optionsDiv != null) {
						option = selectSingleOptionIndex(optionIndex, optionsDiv);
						if (option != null && isSingleDropDownValueSelected(getElement(elementName), option)) {
							codeEntered = true;
						}
					}
				} catch (Exception ex) {
					System.out.println("Exception came in selectSingleOptionFromDropDown (String elementName, " +
							"Integer optionIndex, boolean table) due to : "+ex.getMessage());
				}
				count++;
			} while (!codeEntered && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
			isOptionSelected(getElement(elementName), option);
		}
	}

	public void selectMultiOptionFromDropDown(WebElement element, List<Integer> options, boolean table)	throws Exception {
		// options null means select all
		if (WebElementHelper.isEnabled(element)) {
			WebElement optionsDiv = null;
			int count = 0;
			do {
				clickSelectElement(element);
				optionsDiv = getMultiOptionsElement(table);
				if (optionsDiv != null) {
					selectMultiOptions(options, optionsDiv);
				}
				count++;
			} while (optionsDiv == null && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
			removeBackDrop(element);
		}
	}


	public void enterDateValue(WebElement dateElement, String givenDate) throws Exception {
		int count = 0;
		do {
			try {
				CommonMethods.setAttributeValue(dateElement, "");
				dateElement.clear();
				CommonMethods.setAttributeValue(dateElement, givenDate);
				WaitHelper.waitFor(500);
				dateElement.sendKeys(Keys.chord(Keys.CONTROL, "a"), givenDate);
				WaitHelper.waitFor(500);
				minimizeDateCalender();
			} catch (Exception ex) {
				count++;
			}
			count++;
		} while (!dateElement.getAttribute("value").trim().equalsIgnoreCase(givenDate)
				&& count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
		if (!dateElement.getAttribute("value").trim().equalsIgnoreCase(givenDate)) {
			throw new Exception("date entered " + dateElement.getAttribute("value") + " is mismatching with given date "
					+ givenDate);
		}

	}


	public void enterDateOnPopup(WebElement calendarElement, WebElement calendarInputElement, String givenDate) throws Exception {
		WebElementHelper.clickElement(getElement(calendarElement, "date_calendar_open"));
		SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DEFAULT_DATE_FORMAT);
		Date toBeEntered = sdf.parse(givenDate);
		String month = new SimpleDateFormat("MMMM").format(toBeEntered);
		String year = new SimpleDateFormat("YYYY").format(toBeEntered);
		WebElement dateCalendarDiv = getElement("date_calendar");
		WebElement monthElement = getElement(dateCalendarDiv, "date_month_select");
		selectDateOptionFromDropDown(monthElement, month);
		WebElement yearElement = getElement(dateCalendarDiv, "date_year_select");
		selectDateOptionFromDropDown(yearElement, year);
		String fullDate = new SimpleDateFormat(DateHelper.CALENDAR_DATE_FORMAT).format(toBeEntered);
		WebElement dayElement = getElement(dateCalendarDiv, "td[aria-label='"+fullDate+"']", "css");
		WebElementHelper.clickElement(dayElement);
		WaitHelper.waitFor(1000);
		if (!calendarInputElement.getAttribute("value").trim().equalsIgnoreCase(givenDate)) {
			throw new Exception("date entered " + calendarInputElement.getAttribute("value") + " is mismatching with given date "
					+ givenDate);
		}
	}


	public void minimizeDateCalender() throws Exception {
		WaitHelper.waitFor(1000);
		getElementWithoutWait("calendarComponent").sendKeys(Keys.ESCAPE);
		WaitHelper.waitFor(1000);
	}

	public void enterInputElementDetails(WebElement element, String value) throws Exception {
		try {
			if (WebElementHelper.isEnabled(element)) {
				try {
					element.click();
				} catch (Exception ex) {
					try {
						WebElementHelper.clickElement(element);
					} catch (Exception exf) {
						WebElementHelper.clickElementUsingJquery(element);
					}
				}
				element.clear();
				if (value != null) {
					WebElementHelper.enterTextInSequence(element, value, true);
				}
				WaitHelper.waitFor(100);
			}
		} catch (Exception ex) {
			throw new Exception("Could not enter value " + value + " bcoz of : " + ex.getMessage());
		}
	}

	public void searchInTopPanel(String type, String textToEnter) throws Exception {
		WebElement btnElement = getElement("search_type");
		if (WebElementHelper.isEnabled(btnElement)) {
			int count = 0;
			boolean codeEntered = false;
			do {
				try{
					if(topPanelPopupOpen(btnElement) != null){
						selectTopPanelOption(type);
						codeEntered = isTopPanelCodeEntered(type, codeEntered);
					}
				}catch(Exception ex){
					codeEntered = false;
				}
				count++;
			} while (!codeEntered && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
			removeBackDrop(btnElement);
		}else{
			throw new Exception("top panel search type is not enabled");
		}
		clickOnSearchActionInTopPanel(textToEnter);
	}

	private void clickOnSearchActionInTopPanel(String textToEnter) throws Exception {
		WebElement searchInput = getElement("search_input");
		WebElementHelper.enterTextWithOutTab(searchInput, textToEnter);
		int count = 0;
		do{
			count++;
			if(count % 2 == 0){
				getElement("search_input").sendKeys(Keys.ENTER);
			}else{
				WebElementHelper.clickElementUsingJquery(getElement("search_panel_icon"));
			}
			clickOnOkButtonInAnyInfoPopup(getProperty("loose_data_check"));
		}while(checkWorkAreaText() && count < WaitHelper.WEBELEMENT_REFRESH_TIMEOUT);
		waitForFullPageLoaderToComplete();
	}

	private boolean isTopPanelCodeEntered(String type, boolean codeEntered) throws Exception {
		try{
			WebElement btnElementSpan = getElement(getElement("search_type"), "span", "tagname");
			String textToCheck = WebElementHelper.getText(btnElementSpan).trim().split("\\s+")[0];
			if (type.equalsIgnoreCase(getProperty("search_skywards_label"))) {
				if (textToCheck.equalsIgnoreCase(getProperty("skywards_text"))) {
					codeEntered = true;
				}
			} else if (type.equalsIgnoreCase(getProperty("search_business_rewards_label"))) {
				if (textToCheck.equalsIgnoreCase(getProperty("business_rewards_text"))) {
					codeEntered = true;
				}
			} else {
				if (textToCheck.equalsIgnoreCase(type)) {
					codeEntered = true;
				}
			}
		}catch (Exception ex){
			throw new Exception("Top panel code entered check failed : "+ex.getMessage());
		}
		return codeEntered;
	}

	private void selectTopPanelOption(String type) throws Exception {
		try{
			WaitHelper.waitUntilExists(this, "search_item_list");
			List<WebElement> optionsList = getElements("search_item_list");
			for (WebElement eachElement : optionsList) {
				if (eachElement.getText().trim().toUpperCase().contains(type.toUpperCase())) {
					WaitHelper.waitUntilClickable(eachElement);
					try {
						WebElementHelper.clickElementUsingJquery(eachElement);
					} catch (Exception ex) {
						try{
							WebElementHelper.clickElement(eachElement);
						}catch (Exception exs){
							WebElementHelper.clickElementUsingJquery(eachElement);
						}
					}
					WaitHelper.waitFor(1000);
					break;
				}
			}
		}catch (Exception ex){
			throw new Exception("Top panel code selection failed : "+ex.getMessage());
		}
	}

	private WebElement topPanelPopupOpen(WebElement btnElement) throws Exception {
		try{
			removeBackDrop(btnElement);
			try {
				WebElementHelper.clickElementUsingJquery(btnElement);
			} catch (Exception ex) {
				try{
					WebElementHelper.clickElement(btnElement);
				}catch (Exception exs){
					WebElementHelper.clickElementUsingJquery(btnElement);
				}
			}
			WaitHelper.waitUntilExists(this, "search_input_div");
			return getElement("search_input_div");
		}catch (Exception ex){
			throw new Exception("Top Panel Pop up Open failed due to : "+ex.getMessage());
		}
	}

	private boolean checkWorkAreaText() throws Exception{
		return getElement("work_area_tab_text").getText().toUpperCase().contains(getProperty("primary_area_label").toUpperCase());
	}

	public void clickOnFloatingPanelBackButton() throws Exception {
		try {
			WebElement floatingPanel = getElement("floating_panel");
			WebElement backButton = getWebElement(floatingPanel, "floating_panel_back_btn", false);
			if (backButton != null) {
				WebElementHelper.clickElementUsingJquery(backButton);
			}
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	public void waitForFullPageLoaderToComplete() throws Exception {
		int count = 0;
		do  {
			WaitHelper.waitFor(1000);
			count++;
		}while(getFullPageLoaderActiveDiv() != null && count < WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT);
		waitForCircularProgressBarToComplete();
	}

	private void waitForCircularProgressBarToComplete() throws Exception {
		int count = 0;
		while (isElementStillPresent("progress_circular") && count < WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT) {
			List<WebElement> circularBar = getElements("progress_circular");
			if (circularBar.size() == 2) {
				break;
			} else {
				WaitHelper.waitFor(1000);
			}
			count++;
		}
	}

	public void waitForLinearProgressBarToComplete() throws Exception {
		int count = 0;
		while (isElementStillPresent("progress_linear") && count < WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT) {
			WebElement progressLinear = getWebElement("progress_linear", false);
			if (progressLinear != null && !WebElementHelper.isHidden(progressLinear)) {
				String mode = progressLinear.getAttribute("md-mode");
				if (mode == null || mode.equalsIgnoreCase("determinate")) {
					break;
				} else {
					WaitHelper.waitFor(1000);
				}
			}else{
				break;
			}
			count++;
		}
	}

	public boolean checkForErrorPopup() throws Exception {
		String errorMessage = getErrorMessageOnPopUp();
		if (!errorMessage.isEmpty()) {
			if (errorMessage.toUpperCase().contains(getProperty("codeshareFlightInformationMessage").toUpperCase())) {
				clickOnOkButtonInAnyInfoPopup(null);
				return false;
			} else {
				throw new Exception(getProperty("error_popup_exception_msg") + errorMessage);
			}
		} else {
			return false;
		}
	}

	public List<WebElement> getListOptions(String locator) throws Exception {
		int seconds = WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		long time = 1000 * seconds;
		List<WebElement> listOptions = null;
		while (time > 0) {
			listOptions = getElements(locator);
			if (listOptions != null && listOptions.size() > 0) {
				break;
			} else {
				Thread.sleep(100);
				time = time - 100;
			}
		}
		return listOptions;
	}

	public void selectOptionFromMdSelectComponent(WebElement mdSelectElementIdentifier,
												  List<WebElement> mdOptionsElementsIdentifier, String optionToSelect) throws Exception {
		try {
			WebElementHelper.clickElement(mdSelectElementIdentifier);
			WaitHelper.waitFor(2000);
			List<WebElement> allTierElemnts = mdOptionsElementsIdentifier;
			for (WebElement webitems : allTierElemnts) {
				String val = WebElementHelper.getText(webitems);
				if (val.equalsIgnoreCase(optionToSelect)) {
					WebElementHelper.clickElement(webitems);
					break;
				}
			}
			removeBackDrop(mdSelectElementIdentifier);
		} catch (Exception ex) {
			throw new Exception("Could not select from drop down option\n" + ex.getMessage());
		}
	}

	public boolean floatingPanelWithTextDisplayed(String textToCheck) throws Exception {
		try{
			WaitHelper.waitFor(2000);
			WaitHelper.waitUntilExists(this, "floating_panel");
			List<WebElement> floatingPanels = getElements("floating_panel");
			for (WebElement floatingPanel : floatingPanels) {
				WaitHelper.waitUntilElementRefreshed(floatingPanel);
				WaitHelper.waitUntilExists(floatingPanel, this, "floating_panel_back_btn");
				WaitHelper.waitUntilExists(floatingPanel, this, "floating_panel_text");
			}
			WebElement floatingPanel = getElement("floating_panel");
			WebElement floatingPanelText = getElement(floatingPanel, "floating_panel_text");
			WaitHelper.waitUntilVisible(floatingPanelText);
			if (WebElementHelper.getText(floatingPanelText).toUpperCase().contains(textToCheck.toUpperCase())) {
				waitForFullPageLoaderToComplete();
				return true;
			} else {
				throw new Exception("Floating panel page with text : " + getProperty(textToCheck) + " is not displayed");
			}
		}catch (Exception ex){
			System.out.println("floating panel check failed for : "+textToCheck+" due to : "+ex.getMessage());
			return false;
		}
	}

	public void clickONPopUPwithSubmitButton() throws Exception {
		try {
			WaitHelper.waitUntilExists(this, "btn_done");
			WebElementHelper.clickElement(getElement("btn_done"));
		} catch (Exception ex) {
			throw new Exception("Could not click on Submit button \n" + ex.getMessage());
		}
	}

	private WebElement getFullPageLoaderActiveDiv() throws Exception {
		List<WebElement> fullPageLoaderElements = getElements("full_page_loader_div");
		WebElement fullPageLoadedDiv = null;
		for(WebElement element : fullPageLoaderElements){
			if(!WebElementHelper.isHidden(element)){
				fullPageLoadedDiv = element;
				break;
			}
		}
		return fullPageLoadedDiv;
	}



	public String getToolTipText() throws Exception{
		try{
			WebElement toolTipTag = getElement("tool_tip_tag");
			WebElement toolTipDiv = getWebElement(toolTipTag, "div", "tagname", false);
			return toolTipDiv.getText().trim();
		}catch(Exception ex){
			throw new Exception("Tool tip is not displayed");
		}
	}

	public void closeToastMsg() throws Exception{
		try{
			WaitHelper.waitFor(1000);
			WebElement toastCloseBtn = getWebElement("toast_close_btn", false);
			if(toastCloseBtn != null){
				try{
					WebElementHelper.clickElementUsingJquery(toastCloseBtn);
				}catch(Exception ex){
					WebElementHelper.clickElement(toastCloseBtn);
				}
			}
			WaitHelper.waitFor(1000);
		}catch (Exception ex){
			throw new Exception("Close button not displayed on toast : "+ex.getMessage());
		}
	}


	public void selectDateOptionFromDropDown(WebElement element, String option) throws Exception {
		if (option != null && WebElementHelper.isEnabled(element) && !isSingleDropDownValueSelected(element, option)) {
			int count = 0;
			boolean codeEntered = false;
			do {
				try {
					clickSelectElement(element);
					WebElement optionsDiv = getElement("date_menu_options_div");
					if (optionsDiv != null) {
						selectSingleOptionName(option, optionsDiv);
						if (isSingleDropDownValueSelected(element, option)) {
							codeEntered = true;
						}
					}
				} catch (Exception ex) {
					throw new Exception("Error while selecting Single Option From date DropDown " + ex.getMessage());
				}
				count++;
			} while (!codeEntered && count < WaitHelper.WEBELEMENT_DEFAULT_INPUT_TRY_COUNT);
			isOptionSelected(element, option);
		}
	}

	public boolean checkCardElementExistsInShowMore(String cardIconName) throws Exception {
		try{
			List<WebElement> closedPopupOptions = getClosedPopupOptions();
			for(WebElement tableCloseElement : closedPopupOptions){
				if(WebElementHelper.isHidden(tableCloseElement)){
					WebElement showMoreOptions = getShowMoreOptions(tableCloseElement);
					if(showMoreOptions != null){
						return (getWebElement(showMoreOptions, cardIconName, false) != null);
					}
				}
			}
		}catch (Exception ex){
			throw new Exception("checking card element in show more failed due to : "+ex.getMessage());
		}
		return false;
	}

	public WebElement getCardElementExistsInShowMore(String cardIconName) throws Exception {
		WebElement elementToReturn = null;
		try{
			WebElementHelper.clickElementUsingJquery(getShowMoreElement());
		}catch (Exception ex){
			throw new Exception("clicking show more expand icons failed due to : "+ex.getMessage());
		}
		try{
			WebElement showMoreOptionsElement = getShowMoreOptions(getPopupOptions());
			WebElement cardElement = getWebElement(showMoreOptionsElement, cardIconName, false);
			elementToReturn = getElement(cardElement, "button", "tagname");
		}catch (Exception ex){
			throw new Exception("clicking show more option failed due to : "+ex.getMessage());
		}
		return elementToReturn;
	}

	public List<WebElement> getClosedPopupOptions() throws Exception {
		WaitHelper.waitUntilExists(this, "table_row_btn_menu_close_class");
		return getElements("table_row_btn_menu_close_class");
	}

	public WebElement getPopupOptions() throws Exception {
		WaitHelper.waitUntilExists(this, "table_row_btn_menu_class");
		return getElement("table_row_btn_menu_class");
	}

	public WebElement getShowMoreElement() throws Exception {
		return getWebElement("show_more", false);
	}

	public WebElement getShowMoreOptions(WebElement optionsElement) throws Exception{
		return getWebElement(optionsElement, "show_more_items", false);
	}

	public void clickOnCheckBoxInTableRow(WebElement row) throws Exception {
		WebElement checkBoxElement = getElement(row, "md-checkbox", "tagname");
		WaitHelper.waitFor(500);
		if (WebElementHelper.isEnabled(checkBoxElement)) {
			WebElementHelper.clickElement(checkBoxElement);
		} else {
			throw new Exception("Check box is not enabled : " + checkBoxElement);
		}
	}

	public boolean checkSideBarDisplayed() throws Exception{
		try {
			WebElement sideBarPanel = getElement("side_bar_panel");
			WebElement expandedElement = getElement(sideBarPanel, "side_bar_panel_expanded");
			if(WebElementHelper.isHidden(expandedElement)){
				return false;
			}else{
				return true;
			}
		} catch(Exception ex) {
			return false;
		}
	}


	public void clickTopPanelCardIcon(String cardIconName, String cardCollapseName, String cardAccordianName,
									 boolean hiddenCheck) throws Exception {
		WebElement cardElement;
		try{
			if(checkSideBarDisplayed() && getShowMoreElement() != null && hiddenCheck &&
					checkCardElementExistsInShowMore(cardIconName)){
				cardElement = getCardElementExistsInShowMore(cardIconName);
			}else{
				cardElement = getElement(cardIconName);
			}
		}catch (Exception ex){
			throw new Exception(cardIconName+" card hidden check failed due to : "+ex.getMessage());
		}
		try{
			WebElementHelper.clickElementUsingJquery(cardElement);
		}catch (Exception ex){
			try{
				WebElementHelper.clickElement(cardElement);
			}catch (Exception exs){
				WebElementHelper.clickElementUsingJquery(cardElement);
			}
		}
		waitForFullPageLoaderToComplete();
		try{
			checkCardAccordianExpanded(cardCollapseName, cardAccordianName);
		}catch (Exception ex){
			throw new Exception(cardIconName+" card accordian expand failed due to : "+ex.getMessage());
		}
	}

	public boolean checkCardAccordianExpanded(String cardCollapseName, String cardAccordianName) throws Exception {
		WebElement collapseButton = getWebElement(cardCollapseName, false);
		if (collapseButton != null) {
			String accordianClass = getElement(cardAccordianName).getAttribute("class");
			if (accordianClass.contains(getProperty("accordion_collapsed"))) {
				try{
					WebElementHelper.clickElementUsingJquery(collapseButton);
				}catch (Exception ex){
					try{
						WebElementHelper.clickElement(collapseButton);
					}catch (Exception exs){
						WebElementHelper.clickElementUsingJquery(collapseButton);
					}
				}
				WaitHelper.waitFor(500);
				return true;
			}else if (accordianClass.contains(getProperty("accordion_expanded"))) {
				return true;
			}
		}
		return false;
	}

	public void selectYesForOtherFareBrands(String journeyType, String fareBrandType) throws Exception {
		if (!StringUtils.isBlank(fareBrandType)) {
			if (journeyType.equalsIgnoreCase(getProperty("oneway_label"))) {
				checkAndClickPopupMessageType(getProperty("NotAvailableFareBrandOneway"));
			} else {
				checkAndClickPopupMessageType(getProperty("NotAvailableFareBrandReturn"));
			}
		}
	}

	public void checkAndClickPopupMessageType(String popupMessageToCheck){
		int count = 0;
		try {
			while (checkInfoMessageOnAnyPopUp(popupMessageToCheck)
					&& count < WaitHelper.WEBELEMENT_POPUP_CLICK_TRY_COUNT) {
				clickOnOkButtonInAnyInfoPopup(popupMessageToCheck);
				count++;
			}
		} catch (Exception ex) {
			count++;
		}
	}

	public void selectSideNavigationOption(String navigationLabelText) throws Exception {
		WebElement hamburgerMenu = null;
		WebElement menuListPanel = null;
		List<WebElement> menuList = null;
		try {
			WaitHelper.waitUntilExists(this, "hamburger_menu");
			hamburgerMenu = getElement("hamburger_menu");
			WebElementHelper.clickElement(hamburgerMenu);
			menuListPanel = getElement("hamburger_menu_list_panel");
			menuList = getElements(menuListPanel, "hamburger_menu_list_options");
			for (WebElement element : menuList) {
				if (element.getText().toUpperCase().contains(navigationLabelText.toUpperCase())) {
					try{
						WebElementHelper.clickElement(element);
					}catch(Exception ex){
						WebElementHelper.clickElementUsingJquery(element);
					}
					clickOnOkButtonInAnyInfoPopup(null);
					break;
				}
			}

		} catch (Exception ex) {
			throw new Exception("Error while navigating to the side navigation option  " + ex.getMessage());
		}
	}



}
