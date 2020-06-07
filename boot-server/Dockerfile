#
FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# RUN cp -rf profile/dev src/main/resource && mvn package
ENTRYPOINT ["java", "-jar", "app.jar"]
