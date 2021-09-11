FROM openjdk:17-alpine

COPY build/libs/*.jar /automation/app.jar
ENTRYPOINT ["java","-jar","/automation/app.jar"]
