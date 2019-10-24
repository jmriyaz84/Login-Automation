package com.login.test.steps;

import com.login.test.pages.PageFactory;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class LoginSteps {
    @Given("^User is on Login Page$")
    public void login() throws Exception {
        PageFactory.getLoginPage().isLoginPageLoaded();
    }

    @Then("^enter the username and password$")
    public void enterTheUsernameAndPassword() throws Exception {
        PageFactory.getLoginPage().login();
    }

    @Then("^click register as \"([^\"]*)\" button$")
    public void clickRegisterAsButton(String userType) throws Exception {
        PageFactory.getLoginPage().register(userType);
    }
}
