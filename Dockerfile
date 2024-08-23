FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/RobotWorld-1.0-SNAPSHOT-jar-with-dependencies.jar /app/RobotWorld.jar

ENTRYPOINT ["java", "-jar", "RobotWorld.jar"]

