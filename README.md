This is a fourth project for Open Classrooms - test and debug a java application.

# Parking System
A command line app for managing the parking system. 
This app uses Java to run and stores the data in Mysql DB.

Here is what the app does:

When launching the app, the user is asked to select an action: 
either enter or exit the parking garage (or exit the app).
When the user enters, the system will ask for the type of 
vehicle (car or bike), and the license plate number and will
 let the user enter if a spot is available.
  It will also tell the user where to park.
When exiting the parking garage, the user gives his license plate again.
 The system will then calculate and display the fee based on the parking
  time and vehicle type, then go back to the home menu.

### Running App

Post installation of MySQL, Java and Maven, you will have to set up the 
tables and data in the data base.
For this, please run the sql commands present in the `Data.sql` file 
under the `resources` folder in the code base.

### Project Tasks

* Add a 30-min free-parking discount feature.
* Add a 5% discount for recurring users.
* Correct the code so that all the unit tests pass.
* Complete the integration tests marked by “TODO” comments.

### Implementation

For the first phase - correcting the code and ensuring its reliability through unit testing, I used [FindBugs](https://mvnrepository.com/artifact/org.codehaus.mojo/findbugs-maven-plugin) and the debugging feature of my IDE of choice.

![FindBugs](https://user-images.githubusercontent.com/39421427/95667895-e02ba280-0b74-11eb-9471-358f9a749ec1.jpg)

After fixing the code I implemented unit tests for all public methods in `service`, `model`,
 `dao` and `constants` packages. I used JUnit5 with [Junit Jupiter Api](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)
 and [Mockito Junit Jupiter](https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter).
 The execution report for the tests was delivered by [Surefire](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-surefire-plugin).
 
 ![Surefire](https://user-images.githubusercontent.com/39421427/95667862-92169f00-0b74-11eb-9d0a-323358048c76.jpg)
 
 The goal for test coverage was 60,70% and the actual coverage is 83% what was checked with help of
 [JaCoCo](https://mvnrepository.com/artifact/org.jacoco/jacoco-maven-plugin).
 
 ![JacocoApp](https://user-images.githubusercontent.com/39421427/95667877-b1adc780-0b74-11eb-9c23-6ee24a590cf0.jpg)
 
 <br>I added integration tests as well between `service` and `repository` modules to make sure they work together as expected.
 
 For the second phase - implementing new features, I decided to create a new `enum` `DiscountType`. Implementing options 
 as enums and including option's functionality as abstract method each enum implements for itself is one of the better ways
 to implement `Strategy Design Pattern`.
 <br>I applied this pattern for all `switch` statements in the application as better approach.
 
 Furthermore, I would like to be able to use more than one discount at a time. So I chose to implement a `Builder Design Pattern`
  for the `FareCalculatorService` class. Finally, I included `FareCalculatorService` class as constructor dependency to 
  `InteractiveShell` class which contains the method `loadInterface` that runs the application, so when a user starts 
  application through instantiating `InteractiveShell` he is able at runtime to choose what discount strategy to use as main discount
  and as many as he needs additional discounts.
