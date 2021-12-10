FROM maven:3.8.3-openjdk-16-slim AS build
COPY src /project/src
COPY pom.xml /project/
RUN mvn -f /project/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:16-alpine3.13
ENV mode nothing
COPY --from=build /project/target/kafkadbtransferer-final.jar /dir/app.jar
COPY application-linux.properties /dir/application.properties
ENTRYPOINT java -jar /dir/app.jar --mode=${mode}