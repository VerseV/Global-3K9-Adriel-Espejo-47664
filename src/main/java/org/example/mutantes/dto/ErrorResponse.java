package org.example.mutantes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {

    @Schema(description = "Timestamp del error")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Mensaje de error", example = "Bad Request")
    private String error;

    @Schema(description = "Detalle del error", example = "Invalid DNA sequence")
    private String message;

    @Schema(description = "Path del endpoint", example = "/mutant")
    private String path;
}
