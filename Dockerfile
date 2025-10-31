# Build stage
FROM gradle:8.5.0-jdk17-alpine AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle build --no-daemon

# Package stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/pressing-app.jar .
EXPOSE 8080
CMD ["java", "-jar", "pressing-app.jar"]