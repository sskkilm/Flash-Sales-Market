FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY cart-item /app/cart-item/

WORKDIR /app/cart-item

RUN chmod +x ../gradlew
RUN ../gradlew :cart-item:clean :cart-item:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/cart-item/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]