FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY payment /app/payment/

WORKDIR /app/payment

RUN chmod +x ../gradlew
RUN ../gradlew :payment:clean :payment:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/payment/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]