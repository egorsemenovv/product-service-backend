FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /application
COPY src .
COPY gradle .
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
RUN ./gradlew bootJar

FROM eclipse-temurin:21-jdk-alpine as app
COPY --from=build /build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

