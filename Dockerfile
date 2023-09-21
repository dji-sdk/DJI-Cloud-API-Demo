# Build Stage
FROM maven:latest AS build
COPY src /home/app/src
COPY pom.xml /home/app

RUN apt-get update
RUN apt-get install software-properties-common wget ca-certificates curl gnupg apt-utils  -y
RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add -
RUN add-apt-repository 'deb https://apt.corretto.aws stable main'
RUN apt-get update
RUN apt-get install -y java-21-amazon-corretto-jdk
RUN export JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto

RUN mvn -f /home/app/pom.xml clean package

# Execution Stage
FROM debian:bookworm
COPY --from=build /home/app/target/cloud-api-sample-1.7.0.jar /usr/local/lib/cloud-api-sample-1.7.0.jar
RUN apt-get update
RUN apt-get install software-properties-common wget ca-certificates curl gnupg apt-utils  -y
RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add -
RUN add-apt-repository 'deb https://apt.corretto.aws stable main'
RUN apt-get update
RUN apt-get install -y java-21-amazon-corretto-jdk
RUN export  JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto

EXPOSE 8080
ENTRYPOINT ["/usr/lib/jvm/java-21-amazon-corretto/bin/java","-jar","/usr/local/lib/cloud-api-sample-1.7.0.jar"]

