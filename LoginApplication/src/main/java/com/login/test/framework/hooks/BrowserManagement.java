package com.login.test.framework.hooks;

import com.login.test.framework.core.common.ReadPropertyFile;
import com.login.test.framework.helpers.PropertyHelper;
import com.login.test.pages.PageFactory;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

// manages browser lifecycle
public class BrowserManagement {
	public static WebDriver driver;
	public static Scenario scenario;

	@Before(value = { "~@NoBrowser"}, order = 1, timeout = 60000)
	public static void prepareBrowser() throws Exception {
		try {
			Properties configFileObject = ReadPropertyFile.loadPropertyFile("src/main/resources/config.properties");
			driver = getWebdriver(configFileObject.getProperty("chromeDriver54"));
			driver.navigate().to(PropertyHelper.getBaseUrl());
			PageFactory.setDriver(driver);
			try {
				driver.manage().window().maximize();
			} catch (WebDriverException ex) {
				System.out.println("This version of driver cannot maximise the browser due to : "+ex.getMessage());
			}
		} catch (Exception ex) {
			if (driver != null) {
				driver.close();
			}
		}
	}


	// we want to close browser at the end hence the order is 100
	@After(value = { "~@NoBrowser" }, order = 101)
	public static void updateScreenShot(Scenario scenario) {
		try {
			if (scenario.isFailed()) {
				scenario.embed(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES), "image/png");
			}
		} catch (Exception ex) {
			System.out.println("Error in taking the screen shot");
		}
	}

	@After(value = { "~@NoBrowser"}, order = 99)
	public static void closeBrowser() {
		try {
			driver.close();
			driver.quit();
		} catch (Exception ex) {
			System.out.println("There was an error thrown while driver quit call \n" + ex.getMessage());
		}
	}

	public static WebDriver getWebdriver(String chromeDriverPath) throws Exception {
		WebDriver driver;
		Boolean useGrid = Boolean.valueOf(PropertyHelper.getProperty("GridRun"));
		String browserType = PropertyHelper.getProperty("BrowserType");
		String hubURl = PropertyHelper.getProperty("HubUrl");
		if (useGrid) {
			driver = getGridBrowser(browserType, hubURl);
		} else if (browserType.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			ChromeOptions options = new ChromeOptions();
			if (!StringUtils.isEmpty(PropertyHelper.getProperty("chrome.binary"))) {
				options.setBinary(PropertyHelper.getProperty("chrome.binary"));
			}
			options.addArguments("disable-extensions");
			options.addArguments("disable-infobars");
			options.addArguments("start-maximized");
			options.setExperimentalOption("useAutomationExtension", false);
           driver = new ChromeDriver(options);
		} else if (browserType.equalsIgnoreCase("chromium")) {
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			ChromeOptions options = new ChromeOptions();
			if (!StringUtils.isEmpty(PropertyHelper.getProperty("chromium.binary"))) {
				options.setBinary(PropertyHelper.getProperty("chromium.binary"));
			}
			options.addArguments("--start-maximized");
			driver = new ChromeDriver(options);
		} else if (browserType.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else if (browserType.equalsIgnoreCase("iPad Air") || browserType.equalsIgnoreCase("iPhone 6")) {
			driver = getMobileWebDriver(browserType);
		} else {
			driver = getFirefoxDriver();
		}
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		return driver;
	}

	private static FirefoxDriver getFirefoxDriver() {
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setEnableNativeEvents(false);

		return new FirefoxDriver(firefoxProfile);
	}

	private static WebDriver getMobileWebDriver(String type) {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setBrowserName("Safari");
		desiredCapabilities.setCapability("deviceName", type);
		desiredCapabilities.setCapability("platformName", "iOS");
		desiredCapabilities.setCapability("platformVersion", "8.3");
		desiredCapabilities.setCapability("browserName", "safari");
		desiredCapabilities.setCapability("browserVersion", "8.3");

		String hubUrlString = "http://pc3061572310:4444/wd/hub/";

		URL hubUrl;
		try {
			hubUrl = new URL(hubUrlString);
		} catch (MalformedURLException e) {
			throw new Error(e.getCause());
		}

		RemoteWebDriver remoteWebDriver = new RemoteWebDriver(hubUrl, desiredCapabilities);
		remoteWebDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
		return remoteWebDriver;
	}

	public static WebDriver getGridBrowser(String browserType, String gridHubUrl) {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setBrowserName(browserType);
		if ("chrome".equalsIgnoreCase(browserType)) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-extensions");
			options.addArguments("--start-maximized");
			caps.setCapability(ChromeOptions.CAPABILITY, options);
		} else if ("chromium".equalsIgnoreCase(browserType)) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			caps.setCapability(ChromeOptions.CAPABILITY, options);
		}
		try {
			URL hubUrl = new URL(gridHubUrl);
			RemoteWebDriver remoteWebDriver = new RemoteWebDriver(hubUrl, caps);
			remoteWebDriver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
			return remoteWebDriver;
		} catch (MalformedURLException e) {
			throw new Error(e.getCause());
		}
	}

}
