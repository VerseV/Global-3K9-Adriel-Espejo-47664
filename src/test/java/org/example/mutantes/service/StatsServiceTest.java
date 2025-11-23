package org.example.mutantes.service;

import org.example.mutantes.dto.StatsResponse;
import org.example.mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatsService - Tests de Estadísticas")
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Debe calcular estadísticas correctamente")
    void testGetStats_ReturnsCorrectStats() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(40L, stats.getCount_mutant_dna());
        assertEquals(100L, stats.getCount_human_dna());
        assertEquals(0.4, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular ratio 1.0 cuando mutantes = humanos")
    void testGetStats_RatioEqualsOne() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(50L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(1.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular ratio >1 cuando hay más mutantes que humanos")
    void testGetStats_RatioGreaterThanOne() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(100L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(2.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe retornar 0 cuando no hay registros")
    void testGetStats_NoRecords() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(0L, stats.getCount_mutant_dna());
        assertEquals(0L, stats.getCount_human_dna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("Debe retornar count_mutant cuando no hay humanos")
    void testGetStats_NoHumans() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(40L, stats.getCount_mutant_dna());
        assertEquals(0L, stats.getCount_human_dna());
        assertEquals(40.0, stats.getRatio(), 0.001);  // Caso especial
    }

    @Test
    @DisplayName("Debe retornar 0 cuando solo hay humanos")
    void testGetStats_OnlyHumans() {
        // Arrange
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // Act
        StatsResponse stats = statsService.getStats();

        // Assert
        assertEquals(0L, stats.getCount_mutant_dna());
        assertEquals(100L, stats.getCount_human_dna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }
}