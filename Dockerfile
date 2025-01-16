# Primera etapa: Construcción con Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Segunda etapa: Imagen ligera para ejecutar el JAR
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copia el archivo JAR desde la etapa de construcción
COPY --from=build /app/target/prueba-fullstack-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
