package org.example.mutantes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MutantDetector - Tests del Algoritmo")
class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // ==================== CASOS MUTANTES ====================

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
    void testMutantWithHorizontalAndDiagonalSequences() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // Horizontal: CCCC
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantWithVerticalSequences() {
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
    void testMutantWithMultipleHorizontalSequences() {
        String[] dna = {
                "AAAAGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",  // Segunda horizontal: CCCC
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con diagonales ascendentes y descendentes")
    void testMutantWithBothDiagonals() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante en matriz grande (10x10)")
    void testMutantWithLargeDna() {
        String[] dna = {
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA",
                "CCCCTACCCC",  // Horizontal
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AAAAGGATAA"   // Horizontal
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con 2 diagonales ascendentes")
    void testMutantDiagonalInCorner() {
        String[] dna = {
                "AAAA",
                "TAAA",
                "GTAA",
                "CGTA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante cuando toda la matriz es igual")
    void testMutantAllSameCharacter() {
        String[] dna = {
                "AAAA",
                "AAAA",
                "AAAA",
                "AAAA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    // ==================== CASOS HUMANOS ====================

    @Test
    @DisplayName("No debe detectar mutante con solo una secuencia")
    void testNotMutantWithOnlyOneSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",  // Solo una horizontal: TTTT
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante sin secuencias")
    void testNotMutantWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar mutante en matriz 4x4 sin secuencias")
    void testNotMutantSmallDna() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // ==================== VALIDACIONES ====================

    @Test
    @DisplayName("Debe retornar false para DNA null")
    void testNotMutantWithNullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    @DisplayName("Debe retornar false para DNA vacío")
    void testNotMutantWithEmptyDna() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para matriz no cuadrada")
    void testNotMutantWithNonSquareDna() {
        String[] dna = {
                "ATGC",
                "CAG",     // Fila más corta
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para caracteres inválidos")
    void testNotMutantWithInvalidCharacters() {
        String[] dna = {
                "ATGX",  // X es inválido
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para fila null")
    void testNotMutantWithNullRow() {
        String[] dna = {
                "ATGC",
                null,    // Fila null
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para matriz muy pequeña (3x3)")
    void testNotMutantWithTooSmallDna() {
        String[] dna = {
                "ATG",
                "CAG",
                "TTA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Debe detectar mutante incluso con secuencia larga de 6 caracteres")
    void testMutantWithSequenceLongerThanFour() {
        String[] dna = {
                "AAAAAA",  // 6 A's = múltiples secuencias de 4
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));  // Es mutante porque tiene >1 secuencia
    }
}