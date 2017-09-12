## coding-assignment

### PROBLEM STATEMENT
Write a password validation service, meant to be configurable via IoC (using dependency injection engine of your choice).  The service is meant to check a text string for compliance to any number of password validation rules.  The rules currently known are:

* Must consist of a mixture of lowercase letters and numerical digits only, with at least one of each.
* Must be between 5 and 12 characters in length.
* Must not contain any sequence of characters immediately followed by the same sequence.

### IMPLEMENTATION
* using Spring Boot for rapid application ddevelopment.
* using Spring IOC container for Dependency Injection 
* using Maven as a build tool.<br>
  `mvn clean install` will generate a jar file in application target folder.
  `mvn clean test` will run all unit tests
* `PasswordValidationRule` is the inteface we need to implement to add a new validation rule.
* `PasswordValidationService` is the service where we inject all the validation rules and validate the password.
* For dynamically enabling/disabling rules, we will configure applicable rules in `application.properties` file with `password.validation.rules` propery.
* Implemented `PasswordValidationController` as client for password validation service. Its a Rest controller.
