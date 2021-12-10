FROM maven:3.8.3-openjdk-16-slim AS build
COPY src /project/src
COPY pom.xml /project/
RUN mvn -f /project/pom.xml clean package

FROM openjdk:16-alpine3.13
ENV mode nothing
COPY --from=build /project/target/kafkadbtransferer-final.jar app.jar
COPY application.properties application.properties
ENTRYPOINT java -jar app.jar --mode=${mode}