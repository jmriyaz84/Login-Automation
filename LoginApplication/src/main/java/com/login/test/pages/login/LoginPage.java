package com.login.test.pages.login;

import static com.login.test.framework.helpers.ErrorHandler.assertError;

import com.login.test.framework.helpers.WaitHelper;
import com.login.test.framework.helpers.WebElementHelper;
import com.login.test.pages.BasePage;
import com.login.test.pages.PageFactory;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    public void isLoginPageLoaded() throws Exception{
        WaitHelper.waitUntilExists(this,"title");
        if(!getElement("title").getTagName().equalsIgnoreCase("title")){
            assertError("Application not loaded");
        }
    }

    public void login() throws Exception{
        WaitHelper.waitUntilExists(this,"student_userId");
        WebElement userElement =  getElement("student_userId");
        WebElementHelper.enterText(userElement,getProperty("userId_ForLogin"));
        WaitHelper.waitUntilExists(this,"student_password");
        WebElement passwordElement =  getElement("student_password");
        WebElementHelper.enterText(passwordElement,getProperty("password_ForLogin"));
        WaitHelper.waitFor(5000);
    }

    public void register(String userType) throws Exception{
        if(userType.equalsIgnoreCase("Student")){
            WaitHelper.waitUntilExists(this,"register_student");
            WebElement registerStudent =  getElement("register_student");
            WebElementHelper.clickElement(registerStudent);
            fillForm();
        }
        else{
            WaitHelper.waitUntilExists(this,"register_teacher");
            WebElement registerTeacher =  getElement("register_teacher");
            WebElementHelper.clickElement(registerTeacher);
            fillForm();
        }
    }

    private void fillForm() throws Exception{
        WaitHelper.waitUntilExists(this,"firstName");
        WebElement firstName =  getElement("firstName");
        WebElementHelper.enterText(firstName,"Mohammed");

        WebElement lastName =  getElement("lastName");
        WebElementHelper.enterText(lastName,"Riyaz");

        WebElement userId =  getElement("userId");
        WebElementHelper.enterText(userId,"admin");

        WebElement userPassword =  getElement("userPassword");
        WebElementHelper.enterText(userPassword,"admin");
    }
}
