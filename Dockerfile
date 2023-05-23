FROM gradle:7.5.1-jdk17 AS builder 
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY src/ src/
RUN gradle clean assemble 

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
