package org.example.mutantes.service;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Servicio que implementa el algoritmo de detección de mutantes.
 * Un humano es mutante si encuentra MÁS DE UNA secuencia de cuatro letras iguales
 * en forma horizontal, vertical o diagonal.
 */
@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    /**
     * Determina si una secuencia de ADN pertenece a un mutante
     * @param dna Array de strings que representa la matriz NxN de ADN
     * @return true si es mutante, false si no lo es
     */
    public boolean isMutant(String[] dna) {
        // Validación básica
        if (!isValidDna(dna)) {
            return false;
        }

        final int n = dna.length;
        int sequenceCount = 0;

        // Optimización: Convertir a char[][] para acceso O(1)
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Single Pass: recorrer la matriz UNA SOLA VEZ
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Búsqueda Horizontal (→)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination
                    }
                }

                // Búsqueda Vertical (↓)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination
                    }
                }

                // Búsqueda Diagonal Descendente (↘)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination
                    }
                }

                // Búsqueda Diagonal Ascendente (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early Termination
                    }
                }
            }
        }

        return false;  // Solo encontró 0 o 1 secuencia
    }

    /**
     * Valida que el ADN sea correcto:
     * - No null o vacío
     * - Matriz cuadrada NxN
     * - Solo caracteres A, T, C, G
     * - Tamaño mínimo 4x4
     */
    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        if (dna.length < SEQUENCE_LENGTH) {
            return false;
        }

        final int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false;
            }

            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica secuencia horizontal (→)
     */
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    /**
     * Verifica secuencia vertical (↓)
     */
    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    /**
     * Verifica secuencia diagonal descendente (↘)
     */
    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    /**
     * Verifica secuencia diagonal ascendente (↗)
     */
    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}
