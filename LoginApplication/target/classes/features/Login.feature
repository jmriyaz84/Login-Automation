Feature:Login Function

  @TC01_RegisterStudentTest
  Scenario:Register Student
    Given User is on Login Page
    Then click register as "Student" button

  @TC02_RegisterTeacherTest
  Scenario:Verify Teacher
    Given User is on Login Page
    Then click register as "Teacher" button

  @TC03_LoginTest
  Scenario:Verify Login
    Given User is on Login Page
    Then enter the username and password