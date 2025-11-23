package org.example.mutantes.service;

import org.example.mutantes.entity.DnaRecord;
import org.example.mutantes.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MutantService - Tests de Orquestación y Caché")
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    private String[] mutantDna;
    private String[] humanDna;

    @BeforeEach
    void setUp() {
        mutantDna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        humanDna = new String[]{
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
    }

    @Test
    @DisplayName("Debe analizar y guardar DNA mutante cuando no existe en BD")
    void testAnalyzeDna_SavesMutant_WhenNotCached() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);
        when(dnaRecordRepository.save(any(DnaRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        boolean result = mutantService.analyzeDna(mutantDna);

        // Assert
        assertTrue(result);
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar y guardar DNA humano cuando no existe en BD")
    void testAnalyzeDna_SavesHuman_WhenNotCached() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);
        when(dnaRecordRepository.save(any(DnaRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        boolean result = mutantService.analyzeDna(humanDna);

        // Assert
        assertFalse(result);
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado sin re-analizar (mutante)")
    void testAnalyzeDna_ReturnsCachedResult_ForMutant() {
        // Arrange
        DnaRecord cachedRecord = new DnaRecord("hash123", true);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        // Act
        boolean result = mutantService.analyzeDna(mutantDna);

        // Assert
        assertTrue(result);
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any());  // No debe llamar al detector
        verify(dnaRecordRepository, never()).save(any());  // No debe guardar
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado sin re-analizar (humano)")
    void testAnalyzeDna_ReturnsCachedResult_ForHuman() {
        // Arrange
        DnaRecord cachedRecord = new DnaRecord("hash456", false);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        // Act
        boolean result = mutantService.analyzeDna(humanDna);

        // Assert
        assertFalse(result);
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe calcular hash consistente para el mismo DNA")
    void testCalculateDnaHash_IsConsistent() {
        // Arrange
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);

        // Act
        mutantService.analyzeDna(mutantDna);
        mutantService.analyzeDna(mutantDna);

        // Assert - debe llamarse 2 veces con el mismo hash
        verify(dnaRecordRepository, times(2)).findByDnaHash(anyString());
    }
}