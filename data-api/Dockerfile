FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/target/data-api-0.0.1-SNAPSHOT.jar"]
