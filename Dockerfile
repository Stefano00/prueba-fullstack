FROM openjdk:17-jdk-alpine

# Copia el JAR compilado dentro de la imagen
COPY target/prueba-fullstack-0.0.1-SNAPSHOT.jar app.jar

# (Opcional) Documentar la exposición del puerto 8080
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "/app.jar"]

#mvn clean package
#sudo systemctl stop postgresql
#docker build -t usuario/prueba-fullstack:v1 .
#docker compose down
#docker compose rm
#docker compose up --build
	
