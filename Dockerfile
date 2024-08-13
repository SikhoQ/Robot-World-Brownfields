#
#FROM openjdk:11-jre-slim
#
#WORKDIR /app
#
#COPY target/RobotWorld-1.0-SNAPSHOT-jar-with-dependencies.jar /app/RobotWorld.jar
#
#ENTRYPOINT ["java", "-jar", "RobotWorld.jar"]

FROM openjdk:11-jre-slim

# Set non-interactive mode for apt-get
ENV DEBIAN_FRONTEND=noninteractive

# Install any necessary packages (if required)
RUN apt-get update && apt-get install -y <package-name> && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the JAR file into the container
COPY target/RobotWorld-1.0-SNAPSHOT-jar-with-dependencies.jar /app/RobotWorld.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "RobotWorld.jar"]
