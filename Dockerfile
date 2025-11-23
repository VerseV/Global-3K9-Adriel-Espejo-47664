# ========================================
# ETAPA 1: BUILD
# ========================================
FROM alpine:latest AS build

# Instalar OpenJDK 17 y Bash (útil para gradlew)
RUN apk update && apk add openjdk17 bash

# Establecer directorio de trabajo para mantener orden
WORKDIR /app

# Copiar todo el código
COPY . .

# Dar permisos al wrapper
RUN chmod +x ./gradlew

# Generar el JAR.
# Usamos 'clean' para asegurar que no haya residuos y 'bootJar' para el ejecutable
RUN ./gradlew clean bootJar --no-daemon

# ========================================
# ETAPA 2: RUNTIME
# ========================================
# Usamos Eclipse Temurin (la alternativa oficial y estable a OpenJDK)
# Usamos la versión JRE (Java Runtime Environment) que es más ligera
FROM eclipse-temurin:17-jre-alpine

# Directorio de trabajo en el contenedor final
WORKDIR /app

# Exponer el puerto
EXPOSE 8080

# COPIA INTELIGENTE:
# Usamos *.jar para que copie CUALQUIER jar que se haya generado,
# sin importar si se llama 'ExamenMercado-1.0.jar' o 'mutantes-0.0.1.jar'
COPY --from=build /app/build/libs/*.jar app.jar

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]



