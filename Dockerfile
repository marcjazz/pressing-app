# Build stage
FROM gradle:8.5.0-jdk21-alpine AS build
ENV JAVA_HOME=/opt/java/openjdk
WORKDIR /home/gradle/src
COPY . .
RUN gradle clean build --no-daemon && ls -l build/libs

# Package stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/src/pressing-web/build/libs/pressing-app-0.0.1.jar .
EXPOSE 8080
CMD ["java", "-jar", "pressing-app-0.0.1.jar"]