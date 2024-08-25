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

Execute SQLite . tables
sqlite> .tables
This will display:
objects world

Execute the SQLite . headers
sqlite> .headers on

Execute the SQLite . column
sqlitee> .mode column

Execute the SQLite . width
sqlite> .width 16 16 16 16

Select a table from which you want to retrieve data
sqlite> SELECT * FROM objects; ##to see the objects of the worlds in the database
sqlite> SELECT * FROM world; ##to see worlds in the database


