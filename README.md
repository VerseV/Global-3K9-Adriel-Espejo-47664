🧬 Examen MercadoLibre - Mutant Detector API

Alumno: Adriel Espejo

Legajo: 47664

Materia: Desarrollo de Software

Comisión: 3K9

UTN Mendoza

🔗 Enlaces de Despliegue (Render)

La API se encuentra desplegada y operativa en la plataforma Render:

API Base URL: https://examenmercado-3k9.onrender.com

Documentación Interactiva (Swagger UI): https://examenmercado-3k9.onrender.com/swagger-ui.html

(Recomendado para probar los endpoints)

Estadísticas: https://examenmercado-3k9.onrender.com/stats

📝 Descripción del Proyecto

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men. Este proyecto es una API REST que detecta si un humano es "mutante" basándose en su secuencia de ADN.

Lógica de Negocio:
Un humano es considerado mutante si se encuentran más de una secuencia de cuatro letras iguales (A, T, C, G), de forma oblicua, horizontal o vertical.

Niveles Completados:

✅ Algoritmo de detección: Lógica eficiente para analizar matrices NxN.

✅ API REST: Servicio hosteado en Render.

✅ Base de Datos y Estadísticas: Persistencia en H2 y endpoint de estadísticas.

🛠 Tecnologías Utilizadas

Lenguaje: Java 17 (Eclipse Temurin)

Framework: Spring Boot 3.2.0

Base de Datos: H2 Database (En memoria)

Documentación: OpenAPI (Swagger)

Testing: JUnit 5, Mockito, JaCoCo

Contenedorización: Docker (Multi-stage build)

Despliegue: Render Cloud

📡 Guía de Uso de la API

1. Detectar Mutante

Verifica si una secuencia de ADN corresponde a un mutante.

Método: POST

URL: /mutant

Body (JSON):

{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}


Respuestas:

200 OK: Es Mutante.

403 Forbidden: Es Humano (No mutante).

400 Bad Request: Datos de entrada inválidos (matriz no cuadrada, caracteres erróneos, etc.).

2. Obtener Estadísticas

Devuelve un JSON con las estadísticas de las verificaciones de ADN.

Método: GET

URL: /stats

Respuesta (JSON):

{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}


🚀 Ejecución Local

Prerrequisitos

Java 17 JDK

Gradle (o usar el wrapper incluido)

Pasos

Clonar el repositorio:

git clone <URL_DEL_REPOSITORIO>
cd ExamenMercado-3K9


Ejecutar la aplicación:

./gradlew bootRun


Acceder:

Swagger: http://localhost:8080/swagger-ui.html

Consola H2: http://localhost:8080/h2-console

Testing y Cobertura

Para ejecutar los tests y generar el reporte de cobertura de JaCoCo:

./gradlew test jacocoTestReport


El reporte se generará en: build/reports/jacoco/test/html/index.html

🐳 Ejecución con Docker

Si tienes Docker instalado, puedes crear la imagen y ejecutar el contenedor localmente:

Construir la imagen:

docker build -t mutantes-api .


Ejecutar el contenedor:

docker run -p 8080:8080 mutantes-api


La API estará disponible en http://localhost:8080.

🏛 Arquitectura y Diseño

El proyecto sigue una arquitectura en capas para asegurar escalabilidad y mantenibilidad:

Controller Layer: Maneja las peticiones HTTP (MutantController).

Service Layer: Contiene la lógica de negocio y orquestación (MutantService, StatsService).

Repository Layer: Interacción con la base de datos (DnaRecordRepository).

Domain/Entity: Representación de los datos (DnaRecord).

Optimizaciones Implementadas:

Algoritmo Eficiente: Uso de terminación temprana (Early Termination) al encontrar más de una secuencia, evitando recorrer toda la matriz innecesariamente.

Deduplicación: Se calcula un Hash (SHA-256) de la secuencia de ADN para usarlo como índice único en la base de datos, evitando re-analizar ADNs ya verificados.
