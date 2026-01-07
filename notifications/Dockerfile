FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the parent pom.xml
COPY pom.xml .

COPY user-management/pom.xml ./user-management/
COPY cdn/pom.xml ./cdn/
COPY notifications/pom.xml ./notifications/

RUN mvn dependency:go-offline -B -pl notifications

COPY notifications/src ./notifications/src/

RUN mvn package spring-boot:repackage -DskipTests -pl user-management

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/notifications/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]