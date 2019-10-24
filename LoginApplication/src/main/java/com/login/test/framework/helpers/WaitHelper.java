package com.login.test.framework.helpers;

import com.login.test.pages.BasePage;
import com.login.test.pages.PageFactory;
import com.google.common.base.Stopwatch;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WaitHelper {

	public static final int WEBELEMENT_DEFAULT_TIMEOUT = 25;
	public static final int WEBELEMENT_REFRESH_TIMEOUT = 6;
	public static final int WEBELEMENT_DEFAULT_INPUT_TRY_COUNT = 9;
	public static final int WEBELEMENT_POPUP_TIMEOUT = 4;
	public static final int WEBELEMENT_POPUP_CLICK_TRY_COUNT = 2;
	public static final int BUSINESS_SPECIAL_CLICK_TRY_COUNT = 5;



	public static void waitFor(Integer timeout) throws InterruptedException {
		Thread.sleep(timeout);
	}

	public static void waitUntilVisible(WebElement element) {
		try {
			if (element != null) {
				WebDriver driver = PageFactory.getDriver();
				WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_DEFAULT_TIMEOUT);
				wait.until(ExpectedConditions.visibilityOf(element));
			}
		} catch (Exception ex) {
			System.out.println("wait until visible " + element.getAttribute("name") + " : failed more than "
					+ WEBELEMENT_DEFAULT_TIMEOUT + " seconds");
		}
	}

	public static void waitUntilExists(BasePage basePage, String propertyName) throws Exception {
		Stopwatch timer = Stopwatch.createUnstarted();
		Boolean timeout = false;
		WebElement element = null;
		timer.start();
		do {
			try {
				element = basePage.getWebElement(propertyName, false);
			} catch (Exception ex) {
			}
			if (timer.toString().contains("min")) {
				if (Integer.parseInt(timer.toString().split("\\W+")[0]) >= 1) {
					timeout = true;
				}
			}
		} while (!timeout && element == null);
		timer.stop();
		if (element == null) {
			throw new Exception("wait until exists " + propertyName + " : failed more than " + timer);
		}
	}

	public static void waitUntilExists(String elementLocator, String byType) throws Exception {
		Stopwatch timer = Stopwatch.createUnstarted();
		Boolean timeout = false;
		WebElement element = null;
		timer.start();
		do {
			try {
				element = new BasePage().getElement(elementLocator, byType);
			} catch (Exception ex) {
			}
			if (timer.toString().contains("min")) {
				if (Integer.parseInt(timer.toString().split("\\W+")[0]) >= 1) {
					timeout = true;
				}
			}
		} while (!timeout && element == null);
		timer.stop();
		if (element == null) {
			throw new Exception("wait until exists " + elementLocator + " : failed more than " + timer);
		}
	}

	public static void waitUntilExists(WebElement parent, String elementLocator, String byType) throws Exception {
		Stopwatch timer = Stopwatch.createUnstarted();
		Boolean timeout = false;
		WebElement element = null;
		timer.start();
		do {
			try {
				element = new BasePage().getElement(parent, elementLocator, byType);
			} catch (Exception ex) {
			}
			if (timer.toString().contains("min")) {
				if (Integer.parseInt(timer.toString().split("\\W+")[0]) >= 1) {
					timeout = true;
				}
			}
		} while (!timeout && element == null);
		timer.stop();
		if (element == null) {
			throw new Exception("wait until exists " + elementLocator + " : failed more than " + timer);
		}
	}

	public static void waitUntilExists(WebElement parent, BasePage basePage, String propertyName) throws Exception {
		Stopwatch timer = Stopwatch.createUnstarted();
		Boolean timeout = false;
		WebElement element = null;
		timer.start();
		do {
			try {
				element = basePage.getWebElement(parent, propertyName, false);
			} catch (Exception ex) {
			}
			if (timer.toString().contains("min")) {
				if (Integer.parseInt(timer.toString().split("\\W+")[0]) >= 1) {
					timeout = true;
				}
			}
		} while (!timeout && element == null);
		timer.stop();
		if (element == null) {
			throw new Exception("wait until exists " + propertyName + " : failed more than " + timer);
		}
	}

	public static void waitUntilClickable(WebElement element) {
		try {
			if (element != null) {
				WebDriver driver = PageFactory.getDriver();
				WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_DEFAULT_TIMEOUT);
				wait.until(ExpectedConditions.elementToBeClickable(element));
			}
		} catch (Exception ex) {
			System.out.println("wait until clickable " + element.getAttribute("name") + " : failed more than "
					+ WEBELEMENT_DEFAULT_TIMEOUT + " seconds");
		}
	}

	public static void waitUntilStalenessOf(WebElement element) {
		try {
			if (element != null) {
				WebDriver driver = PageFactory.getDriver();
				WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_DEFAULT_TIMEOUT);
				wait.until(ExpectedConditions.stalenessOf(element));
			}
		} catch (Exception ex) {
			System.out.println("wait until stale ness " + element.getAttribute("name") + " : failed more than "
					+ WEBELEMENT_DEFAULT_TIMEOUT + " seconds");
		}
	}

	public static void waitUntilElementDisappears(String elementName, String type) throws Exception {
		int timeout = 100 * WaitHelper.WEBELEMENT_DEFAULT_TIMEOUT;
		WebDriver driver = PageFactory.getDriver();
		boolean isVisible = true;
		while (isVisible && timeout > 0) {
			try {
				if (type.equalsIgnoreCase("id")) {
					driver.findElement(By.id(elementName));
				} else if (type.equalsIgnoreCase("css")) {
					driver.findElement(By.cssSelector(elementName));
				} else if (type.equalsIgnoreCase("class")) {
					driver.findElement(By.className(elementName));
				} else if (type.equalsIgnoreCase("partialLink")) {
					driver.findElement(By.partialLinkText(elementName));
				} else if (type.equalsIgnoreCase("xpath")) {
					driver.findElement(By.xpath(elementName));
				} else if (type.equalsIgnoreCase("name")) {
					driver.findElement(By.name(elementName));
				} else if (type.equalsIgnoreCase("tagname")) {
					driver.findElement(By.tagName(elementName));
				}
				waitFor(100);
				timeout = timeout - 100;
			} catch (Exception ex) {
				isVisible = false;
				waitFor(100);
			}
		}
	}

	public static boolean waitForJStoLoad() {
		WebDriver driver = PageFactory.getDriver();
		WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_DEFAULT_TIMEOUT);
		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
				} catch (Exception ex) {
					return true;
				}
			}
		};
		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}


	public static void waitUntilElementRefreshed(WebElement element) {
		try {
			if (element != null) {
				WebDriver driver = PageFactory.getDriver();
				WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_REFRESH_TIMEOUT);
				wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeSelected(element)));
			}
		} catch (Exception ex) {
			System.out.println("wait until refreshed " + element.getAttribute("name") + " : failed more than "
					+ WEBELEMENT_REFRESH_TIMEOUT + " seconds");
		}
	}
}