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

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Mutant Detector API")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(summary = "Verificar si un ADN es mutante")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Es mutante",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "No es mutante",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ADN inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        log.info("POST /mutant - Verificando ADN");

        boolean isMutant = mutantService.analyzeDna(request.getDna());

        if (isMutant) {
            log.info("Resultado: MUTANTE");
            return ResponseEntity.ok().build();
        } else {
            log.info("Resultado: HUMANO");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de verificaciones")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas",
                    content = @Content(schema = @Schema(implementation = StatsResponse.class))
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        log.info("GET /stats - Obteniendo estadísticas");

        StatsResponse stats = statsService.getStats();

        log.info("Estadísticas: Mutantes={}, Humanos={}, Ratio={}",
                stats.getCount_mutant_dna(),
                stats.getCount_human_dna(),
                stats.getRatio());

        return ResponseEntity.ok(stats);
    }
}