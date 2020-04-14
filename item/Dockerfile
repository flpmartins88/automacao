FROM openjdk:14-alpine

COPY build/libs/*.jar /item/app.jar
ENTRYPOINT ["java","-jar","/item/app.jar"]