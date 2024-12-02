FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY product /app/product/

WORKDIR /app/product

RUN chmod +x ../gradlew
RUN ../gradlew :product:clean :product:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/product/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]