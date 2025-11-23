package org.example.mutantes.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0.0")
                        .description("""
                                API REST para detectar mutantes mediante análisis de secuencias de ADN.
                                
                                **Funcionalidades:**
                                - Verificación de ADN mutante (POST /mutant)
                                - Estadísticas de verificaciones (GET /stats)
                                
                                **Reglas de Detección:**
                                Un humano es mutante si su ADN contiene MÁS DE UNA secuencia de 4 letras iguales,
                                ya sea de forma horizontal, vertical o diagonal.
                                
                                **Validaciones:**
                                - Matriz cuadrada NxN (mínimo 4x4)
                                - Solo caracteres válidos: A, T, C, G
                                """)
                        .contact(new Contact()
                                .name("Examen MercadoLibre")
                                .url("https://github.com/tu-usuario/mutantes"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}