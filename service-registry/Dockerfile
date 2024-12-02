FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY service-registry /app/service-registry/

WORKDIR /app/service-registry

RUN chmod +x ../gradlew
RUN ../gradlew :service-registry:clean :service-registry:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/service-registry/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]