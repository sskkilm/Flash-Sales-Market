FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY order /app/order/

WORKDIR /app/order

RUN chmod +x ../gradlew
RUN ../gradlew :order:clean :order:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/order/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]