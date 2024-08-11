FROM ubuntu:latest
LABEL authors="stewart"

FROM openjdk:17-jdk-slim

WORKDIR /backend-app

COPY target/ADProjrct-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "ADProjrct-0.0.1-SNAPSHOT.jar"]