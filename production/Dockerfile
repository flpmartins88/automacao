FROM openjdk:14-alpine

COPY build/libs/*.jar /production/app.jar
ENTRYPOINT ["java","-jar","/production/app.jar"]