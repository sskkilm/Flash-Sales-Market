FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY api-gateway /app/api-gateway/

WORKDIR /app/api-gateway

RUN chmod +x ../gradlew
RUN ../gradlew :api-gateway:clean :api-gateway:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/api-gateway/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]