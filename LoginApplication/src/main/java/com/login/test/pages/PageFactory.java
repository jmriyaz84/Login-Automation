package com.login.test.pages;

import com.login.test.pages.login.LoginPage;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver driverInstance) {
        driver = driverInstance;
    }

    public static LoginPage getLoginPage() {
        return new LoginPage();
    }

}

