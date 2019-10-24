package com.login.test.runCakes;

import com.login.test.framework.tags.Login;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/html-reports/TC02_RegisterTeacherTest",
                "json:target/cucumber-report/TC02_RegisterTeacherTest.json"},
        features = {"src/main/resources/features/Login.feature"},
        glue = {"com.login.test"},
        tags = {"@TC02_RegisterTeacherTest"})
@Category(Login.class)
public class TC02_RegisterTeacherTest {

}
