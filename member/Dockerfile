FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle/

COPY member /app/member/

WORKDIR /app/member

RUN chmod +x ../gradlew
RUN ../gradlew :member:clean :member:bootJar

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /app/member/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]