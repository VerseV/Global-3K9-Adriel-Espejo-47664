package org.example.mutantes.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.mutantes.validation.ValidDnaSequence;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para verificar si una secuencia de ADN pertenece a un mutante")
public class DnaRequest {

    @Schema(
            description = "Secuencia de ADN representada como una matriz NxN de strings",
            example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]",
            required = true
    )
    @NotNull(message = "La secuencia de ADN no puede ser nula")
    @NotEmpty(message = "La secuencia de ADN no puede estar vacía")
    @ValidDnaSequence
    private String[] dna;
}