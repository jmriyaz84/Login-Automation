$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/main/resources/features/Login.feature");
formatter.feature({
  "line": 1,
  "name": "Login Function",
  "description": "",
  "id": "login-function",
  "keyword": "Feature"
});
formatter.before({
  "duration": 2887965902,
  "status": "passed"
});
formatter.scenario({
  "line": 4,
  "name": "Register Student",
  "description": "",
  "id": "login-function;register-student",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 3,
      "name": "@TC01_RegisterStudentTest"
    }
  ]
});
formatter.step({
  "line": 5,
  "name": "User is on Login Page",
  "keyword": "Given "
});
formatter.step({
  "line": 6,
  "name": "click register as \"Student\" button",
  "keyword": "Then "
});
formatter.match({
  "location": "LoginSteps.login()"
});
formatter.result({
  "duration": 215221422,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Student",
      "offset": 19
    }
  ],
  "location": "LoginSteps.clickRegisterAsButton(String)"
});
formatter.result({
  "duration": 391147238,
  "status": "passed"
});
formatter.after({
  "duration": 109538,
  "status": "passed"
});
formatter.after({
  "duration": 1136261799,
  "status": "passed"
});
});