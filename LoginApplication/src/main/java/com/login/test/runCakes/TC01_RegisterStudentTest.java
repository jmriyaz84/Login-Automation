package com.login.test.runCakes;

import com.login.test.framework.tags.Login;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/html-reports/TC01_RegisterStudentTest",
                "json:target/cucumber-report/TC01_RegisterStudentTest.json"},
        features = {"src/main/resources/features/Login.feature"},
        glue = {"com.login.test"},
        tags = {"@TC01_RegisterStudentTest"})
@Category(Login.class)
public class TC01_RegisterStudentTest {

}
