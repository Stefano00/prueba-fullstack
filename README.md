# **Backend: Prueba Full Stack**

Este proyecto corresponde al backend de la Prueba Full Stack para Tenpo, encargado de gestionar transacciones y entidades de Tenpistas mediante un backend desarrollado en **Spring Boot**.

---

## **Índice**

1. [Descripción del proyecto](#descripción-del-proyecto)
2. [Características principales](#características-principales)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Requisitos previos](#requisitos-previos)
5. [Ejecución local](#ejecución-local)
6. [Ejecución con Docker](#ejecución-con-docker)
7. [Documentación de la API](#documentación-de-la-api)
8. [Pruebas unitarias](#pruebas-unitarias)
9. [Swagger](#swagger)
10. [Manejo de errores](#manejo-de-errores)
11. [Mejoras pendientes](#mejoras-pendientes)
12. [Créditos](#créditos)


---

## **Descripción del proyecto**

El backend de esta prueba técnica ofrece una API RESTful para:

- **Transacciones**:
  - Crear nuevas transacciones.
  - Listar transacciones existentes.
  - Editar transacciones existentes.
  - Eliminar transacciones.

- **Tenpistas**:
  - Crear nuevos tenpistas.
  - Listar tenpistas existentes.

Está construido utilizando **Spring Boot**, con **PostgreSQL** como base de datos relacional. Incluye validaciones, manejo de errores y documentación de la API con **Swagger**.

---

## **Características principales**

- **API RESTful**: Implementa endpoints para gestionar transacciones y tenpistas.
- **Base de datos relacional**: Utiliza PostgreSQL para el almacenamiento de datos.
- **Rate Limiting**: Limita a 3 solicitudes por minuto por cliente para evitar abusos del sistema.
- **Pruebas unitarias**: Incluye pruebas para servicios, repositorios y controladores.
- **Manejo de errores**: Implementa un manejador global de errores HTTP para respuestas claras.
- **Documentación**: Documenta los endpoints utilizando Swagger/OpenAPI, accesible en `/swagger-ui`.

---

## **Estructura del proyecto**

```bash
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.tenpo
│   │   │       ├── controller
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       └── TenpoApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── data.sql
│   └── test
│       └── java
│           └── com.example.tenpo
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```
---

## **Requisitos previos**

- **Java 17**.
- **Maven**.
- **PostgreSQL**.
- **Docker** y **Docker Compose** (para ejecución con contenedores).

---

## **Ejecución local**

1. **Clonar o descargar** el repositorio:

   ```bash
   git clone https://github.com/Stefano00/prueba-fullstack.git
   cd prueba-fullstack
   ```
#### Configurar la base de datos:
Instalar PostgreSQL y crear la base de datos:
 ```bash
CREATE DATABASE tenpista_db;
 ```
 
Configurar las credenciales en src/main/resources/application.properties:

    spring.datasource.url=jdbc:postgresql://localhost:5432/tenpista_db
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

Construir y ejecutar la aplicación:
 ```bash
mvn clean install
mvn spring-boot:run
 ```
 
La aplicación estará disponible en http://localhost:8080.

---

## **Ejecución con Docker**

### **Ejecutar utilizando Dockerfile**

1. Clonar o descargar el repositorio:

   ```bash
   git clone https://github.com/Stefano00/prueba-fullstack.git
   cd prueba-fullstack
  

#### Construir la imagen de Docker:

    docker build -t tenpo-backend:latest .

    
Ejecutar el contenedor:

    docker run -p 8080:8080 --name tenpo-backend-container -d tenpo-backend:latest

Esto levantará el backend y estará disponible en http://localhost:8080. Asegúrate de que la base de datos PostgreSQL esté configurada y en ejecución antes de iniciar el contenedor.






---


## **Ejecución con Docker Compose**

 **Levantar Front, back y DB con Docker Compose**:
Al descargar el repositorio del front-end: https://github.com/Stefano00/prueba-tenpo-front
y backend: https://github.com/Stefano00/prueba-fullstack
Se debe mover el archivo docker-compose.yml del repositorio https://github.com/Stefano00/prueba-fullstack a una carpeta superior, es decir la estructura de carpetas quedaría de la siguiente manera:

    carpeta_cualquiera/
    ├── docker-compose.yml
    ├── prueba-fullstack/ (repositorio del backend)
    └── prueba-tenpo-front/ (repositorio del frontend)

Ejecutar docker-compose:
    
    docker pull stefano00123/prueba-fullstack:v1
    docker pull stefano00123/prueba-tenpo-front:latest
    docker pull postgres:14-alpine

    docker-compose down -v
    docker-compose build --no-cache
    docker-compose up
    
Esto levantará los siguientes servicios:

Backend: Disponible en http://localhost:8080.

Frontend: Disponible en http://localhost:3000.

Base de datos PostgreSQL: Corriendo en el puerto 5432.
    
    
    NOTA: Asegurarse de que los puertos 3000, 8080 y 5432 estén libres antes de ejecutar los contenedores.
    Si por algún motivo la base de datos postgres está activa, con el siguiente comando se puede detener:
    systemctl stop postgresql
---

## **Documentación de la API**

La documentación de la API está disponible en Swagger UI:

- **URL**: http://localhost:8080/swagger-ui.html

---

## **Pruebas unitarias**

El proyecto incluye pruebas unitarias para servicios, repositorios y controladores. Para ejecutarlas:

```bash
mvn test
```

## **Swagger**

El proyecto incluye un swagger para poder revisar los servicios rest y usarlos:

```bash
`http://localhost:8080/swagger-ui/index.html`
```


---

## **Manejo de errores**

Se implementa un manejador global de errores HTTP que devuelve respuestas estructuradas y claras. Por ejemplo, para un error de servidor, se devuelve un código HTTP 500 con un mensaje descriptivo. Esto facilita la identificación y solución de problemas tanto para los desarrolladores como para los consumidores de la API.

---

## **Mejoras pendientes**

- **Uso de colas SQS para transacciones**: Al existir transacciones, lo ideal sería hacer uso de colas para que en caso de existir errores o algún reset de la aplicación, el mensaje siga encolado y pueda emitirse la solicitud correctamente.
- **Servicio paginado desde el servidor**: Actualmente el servicio GET devuelve un listado, lo correcto sería que devuelva una cantidad límitada pero paginada para reducir el consumo de la base de datos y el rendimiento del sistema.
- **Optimizar manejo de errores**: Mejorar la consistencia y claridad en las respuestas de error.
- **Mejorar documentación con Swagger**: Asegurarse de que todos los endpoints estén documentados y accesibles en `http://localhost:8080/swagger-ui/index.html`.

---

## **Créditos**

- **Autor**: Stefano Marín Quintana
- **Proyecto**: Prueba técnica para Tenpo



