# Build Stage
FROM maven:latest AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Execution Stage
FROM adoptopenjdk/openjdk11:debian
COPY --from=build /home/app/target/cloud-api-sample-1.5.0.jar /usr/local/lib/cloud-api-sample-1.5.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/cloud-api-sample-1.5.0.jar"]

