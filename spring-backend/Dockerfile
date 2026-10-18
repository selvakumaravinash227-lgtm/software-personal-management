FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY spring-backend/pom.xml spring-backend/pom.xml
COPY spring-backend/src spring-backend/src
RUN mvn -B -f spring-backend/pom.xml -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/spring-backend/target/spms-web-api-1.0.0.jar /app/spms-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/spms-api.jar"]
