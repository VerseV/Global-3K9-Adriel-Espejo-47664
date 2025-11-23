package org.example.mutantes.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mutantes.dto.DnaRequest;
import org.example.mutantes.dto.ErrorResponse;
import org.example.mutantes.dto.StatsResponse;
import org.example.mutantes.service.MutantService;
import org.example.mutantes.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST que expone los endpoints de la API de detección de mutantes.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Mutant Detector API", description = "API para detectar mutantes mediante análisis de secuencias de ADN")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    /**
     * POST /mutant
     * Verifica si una secuencia de ADN pertenece a un mutante.
     *
     * @param request DnaRequest con el array de ADN
     * @return 200 OK si es mutante, 403 Forbidden si no es mutante
     */
    @PostMapping("/mutant")
    @Operation(
            summary = "Verificar si un ADN es mutante",
            description = "Recibe una secuencia de ADN y determina si pertenece a un mutante. " +
                    "Un mutante tiene más de una secuencia de 4 letras iguales en forma horizontal, vertical o diagonal."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Es mutante",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No es mutante (humano)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ADN inválido (no cumple formato NxN o caracteres incorrectos)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        log.info("POST /mutant - Verificando ADN...");

        boolean isMutant = mutantService.analyzeDna(request.getDna());

        if (isMutant) {
            log.info("Resultado: MUTANTE ✓");
            return ResponseEntity.ok().build();
        } else {
            log.info("Resultado: HUMANO ✗");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * GET /stats
     * Obtiene estadísticas de las verificaciones de ADN.
     *
     * @return StatsResponse con count_mutant_dna, count_human_dna y ratio
     */
    @GetMapping("/stats")
    @Operation(
            summary = "Obtener estadísticas de verificaciones",
            description = "Retorna las estadísticas de todas las verificaciones de ADN realizadas: " +
                    "cantidad de mutantes, cantidad de humanos y el ratio entre ambos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(schema = @Schema(implementation = StatsResponse.class))
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        log.info("GET /stats - Obteniendo estadísticas...");

        StatsResponse stats = statsService.getStats();

        log.info("Estadísticas: Mutantes={}, Humanos={}, Ratio={}",
                stats.getCount_mutant_dna(),
                stats.getCount_human_dna(),
                stats.getRatio());

        return ResponseEntity.ok(stats);
    }
}
