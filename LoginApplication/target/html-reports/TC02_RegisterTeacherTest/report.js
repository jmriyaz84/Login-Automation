$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/main/resources/features/Login.feature");
formatter.feature({
  "line": 1,
  "name": "Login Function",
  "description": "",
  "id": "login-function",
  "keyword": "Feature"
});
formatter.before({
  "duration": 2799066174,
  "status": "passed"
});
formatter.scenario({
  "line": 9,
  "name": "Verify Teacher",
  "description": "",
  "id": "login-function;verify-teacher",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 8,
      "name": "@TC02_RegisterTeacherTest"
    }
  ]
});
formatter.step({
  "line": 10,
  "name": "User is on Login Page",
  "keyword": "Given "
});
formatter.step({
  "line": 11,
  "name": "click register as \"Teacher\" button",
  "keyword": "Then "
});
formatter.match({
  "location": "LoginSteps.login()"
});
formatter.result({
  "duration": 203051981,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Teacher",
      "offset": 19
    }
  ],
  "location": "LoginSteps.clickRegisterAsButton(String)"
});
formatter.result({
  "duration": 26544486378,
  "error_message": "java.lang.Exception: Element is not found in the page : firstNametest\r\n\tat com.login.test.pages.BasePage.getElement(BasePage.java:138)\r\n\tat com.login.test.pages.login.LoginPage.fillForm(LoginPage.java:47)\r\n\tat com.login.test.pages.login.LoginPage.register(LoginPage.java:41)\r\n\tat com.login.test.steps.LoginSteps.clickRegisterAsButton(LoginSteps.java:21)\r\n\tat ✽.Then click register as \"Teacher\" button(src/main/resources/features/Login.feature:11)\r\n",
  "status": "failed"
});
formatter.embedding("image/png", "embedded0.png");
formatter.after({
  "duration": 387741287,
  "status": "passed"
});
formatter.after({
  "duration": 1129328054,
  "status": "passed"
});
});