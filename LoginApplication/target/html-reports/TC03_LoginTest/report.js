$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/main/resources/features/Login.feature");
formatter.feature({
  "line": 1,
  "name": "Login Function",
  "description": "",
  "id": "login-function",
  "keyword": "Feature"
});
formatter.before({
  "duration": 1778306781,
  "status": "passed"
});
formatter.scenario({
  "line": 14,
  "name": "Verify Login",
  "description": "",
  "id": "login-function;verify-login",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 13,
      "name": "@TC03_LoginTest"
    }
  ]
});
formatter.step({
  "line": 15,
  "name": "User is on Login Page",
  "keyword": "Given "
});
formatter.step({
  "line": 16,
  "name": "enter the username and password",
  "keyword": "Then "
});
formatter.match({
  "location": "LoginSteps.login()"
});
formatter.result({
  "duration": 158266322,
  "status": "passed"
});
formatter.match({
  "location": "LoginSteps.enterTheUsernameAndPassword()"
});
formatter.result({
  "duration": 5962934138,
  "status": "passed"
});
formatter.after({
  "duration": 104615,
  "status": "passed"
});
formatter.after({
  "duration": 3138139955,
  "status": "passed"
});
});