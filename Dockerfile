FROM openjdk:17

# Instala Maven
RUN apt-get update && apt-get install -y maven

# Copia el código fuente de la aplicación a la imagen
COPY . /app

# Establece el directorio de trabajo
WORKDIR /app

# Construye el archivo JAR de la aplicación
RUN mvn clean install -DskipTests

# Ejecuta la aplicación Spring Boot cuando se inicia el contenedor
CMD ["java", "-jar", "target/web-0.0.1-SNAPSHOT.jar"]
