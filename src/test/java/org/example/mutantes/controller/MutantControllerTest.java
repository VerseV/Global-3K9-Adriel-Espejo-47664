package org.example.mutantes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mutantes.dto.DnaRequest;
import org.example.mutantes.dto.StatsResponse;
import org.example.mutantes.service.MutantService;
import org.example.mutantes.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MutantController.class)
@DisplayName("MutantController - Tests de Integración")
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK cuando es mutante")
    void testCheckMutant_ReturnOk_WhenIsMutant() throws Exception {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);
        when(mutantService.analyzeDna(any())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden cuando no es mutante")
    void testCheckMutant_ReturnForbidden_WhenNotMutant() throws Exception {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);
        when(mutantService.analyzeDna(any())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para DNA null")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsNull() throws Exception {
        // Arrange
        DnaRequest request = new DnaRequest(null);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para DNA vacío")
    void testCheckMutant_ReturnBadRequest_WhenDnaIsEmpty() throws Exception {
        // Arrange
        String[] dna = {};
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para matriz no cuadrada")
    void testCheckMutant_ReturnBadRequest_WhenNonSquare() throws Exception {
        // Arrange
        String[] dna = {"ATGC", "CAG", "TTAT"};  // No es cuadrada
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para caracteres inválidos")
    void testCheckMutant_ReturnBadRequest_WhenInvalidCharacters() throws Exception {
        // Arrange
        String[] dna = {"ATGX", "CAGT", "TTAT", "AGAC"};  // X es inválido
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK con estadísticas")
    void testGetStats_ReturnOk_WithStats() throws Exception {
        // Arrange
        StatsResponse stats = new StatsResponse(40L, 100L, 0.4);
        when(statsService.getStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas incluso sin registros")
    void testGetStats_ReturnOk_WithZeroStats() throws Exception {
        // Arrange
        StatsResponse stats = new StatsResponse(0L, 0L, 0.0);
        when(statsService.getStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(0))
                .andExpect(jsonPath("$.count_human_dna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }
}