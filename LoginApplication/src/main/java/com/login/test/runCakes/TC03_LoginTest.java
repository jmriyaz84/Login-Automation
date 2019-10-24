package com.login.test.runCakes;

import com.login.test.framework.tags.Login;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/html-reports/TC03_LoginTest",
                "json:target/cucumber-report/TC03_LoginTest.json"},
        features = {"src/main/resources/features/Login.feature"},
        glue = {"com.login.test"},
        tags = {"@TC03_LoginTest"})
@Category(Login.class)
public class TC03_LoginTest {

}
