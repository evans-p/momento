FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the parent pom.xml
COPY pom.xml .

# Copy the module's pom.xml
COPY user-management/pom.xml ./user-management/
COPY cdn/pom.xml ./cdn/
COPY notifications/pom.xml ./notifications/

RUN mvn dependency:go-offline -B -pl user-management

# Copy the source code of the current module
COPY user-management/src ./user-management/src/

RUN mvn package spring-boot:repackage -DskipTests -pl user-management

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/user-management/target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]