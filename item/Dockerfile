FROM openjdk:15-alpine

COPY build/libs/*.jar /item/app.jar
ENTRYPOINT ["java","-jar","/item/app.jar"]