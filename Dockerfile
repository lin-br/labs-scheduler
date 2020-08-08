FROM openjdk:13-jdk-alpine
VOLUME /opt/application/
ARG JAR_FILE=target/scheduler-app.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]