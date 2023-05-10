FROM eclipse-temurin:17-jdk-alpine
COPY build/libs/realestateagency-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]
