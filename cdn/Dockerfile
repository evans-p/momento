FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the parent pom.xml
COPY pom.xml .

# Copy the module's pom.xml
COPY cdn/pom.xml ./cdn/

RUN mvn dependency:go-offline -B -pl cdn

# Copy the source code of the current module
COPY cdn/src ./cdn/src/

RUN mvn package spring-boot:repackage -DskipTests -pl cdn

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/cdn/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]