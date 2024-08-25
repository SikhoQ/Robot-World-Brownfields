# **Brownfields**

## **Robot Worlds**

Welcome to the Robot World project!

This Robot Worlds project is a continuation of preexisting project.

This project consists of two programs:

Server Program: Manages the world, including obstacles, robots, and other elements.

Client Program: Launches a robot into the world and controls it by sending and receiving messages to/from the server.

### **Prerequisites**

Java 17

Maven

Docker

Javalin

### **Usage**

To run the project, please refer to the Makefile.

Control the Robot:
-Send commands through the client interface.
-Observe updates and responses from the server.

### **Features**

The robot has the ability to look around, move forward, back, turn left, right, and fire.
In addition to the robots ability to attack, it can be attacked. For this, the robot has shield and health

### **Contributing**

Fork the repository.
Create a new branch (git checkout -b feature-xyz).
Commit your changes (git commit -am 'Add feature xyz').
Push to the branch (git push origin feature-xyz).
Create a new Pull Request.

### **To access data**

Install SQLite3
Run the command below:
$ sqlite3 RobotWorlds.db

Execute SQLite . tables\
sqlite> .tables\
This will display:\
objects world

Execute the SQLite . headers\
sqlite> .headers on

Execute the SQLite . column\
sqlitee> .mode column

Execute the SQLite . width\
sqlite> .width 16 16 16 16

Select a table from which you want to retrieve data\
sqlite> SELECT * FROM objects; ##to see the objects of the worlds in the database\
sqlite> SELECT * FROM world; ##to see worlds in the database

## **Codescene Analysis**
1. CodeScene Report Overview
CodeScene was used to analyze the codebase to identify potential areas of improvement and to assess the overall health of the project. The analysis focused on code complexity, hotspots, technical debt, and team contributions. Below are the key findings:

2. Hotspots Identification
CodeScene identified several hotspots in the codebase where code complexity and string heavy function arguments are high, and the tests have large assertion blocks. These hotspots are critical areas that require refactoring to improve code health.

Hotspot: Client.java
Why it’s a Hotspot: Two methods in the Client Class namely handleResponse and processCommand have Code complexity.\
Recommended Action: Consider refactoring to simplify its logic, possibly by breaking it into smaller, more focused classes or methods.

Hotspot: LookTest.java and LaunchRobotTest.java
Why it’s a Hotspot: These tests have high degree of code duplication, primitive obsession, and large assertion blocks.\
Recommended Action: Practice the DRY rule and reduce assertion blocks.


# Testing Documentation

## 1. Overview of Testing Strategy
- **Types of Tests:**
  - Unit Tests\
  Purpose: Unit tests are designed to verify the correctness of individual components or functions in isolation.\
  - Acceptance Tests\
  Purpose: Acceptance tests validate that the entire system meets the business requirements and behaves as expected from an end-user perspective.\
- **Testing Frameworks:**
  - JUnit (Unit and Integration Tests)

## 2. Test Categories and Purpose
- **Unit Tests:**
  - **Purpose:** Validate individual components.
  - **Example:** `CommandTest.java` tests command execution.

- **Acceptance Tests:**
  - **Purpose:** Confirm the system meets requirements.
  - **Example:** `MovementTest.java` tests if the robot moves forward a number of steps.

## 3. How to Run the Tests
- **Running All Tests:**
  - **Command:** `mvn test`











