FROM openjdk:14-alpine

COPY build/libs/*.jar /tag/app.jar
ENTRYPOINT ["java","-jar","/tag/app.jar"]