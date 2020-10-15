FROM openjdk:15-alpine

COPY build/libs/*.jar /automation/app.jar
ENTRYPOINT ["java","-jar","/automation/app.jar"]