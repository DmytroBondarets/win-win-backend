FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/target/auth-api-0.0.1-SNAPSHOT.jar"]
