FROM openjdk:15-alpine

COPY build/libs/*.jar /tag/app.jar
ENTRYPOINT ["java","-jar","/tag/app.jar"]