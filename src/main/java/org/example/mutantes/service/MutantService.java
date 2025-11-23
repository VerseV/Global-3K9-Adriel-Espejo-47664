package org.example.mutantes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mutantes.entity.DnaRecord;
import org.example.mutantes.exception.DnaHashCalculationException;
import org.example.mutantes.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Servicio que orquesta la detección de mutantes y la persistencia de resultados.
 * Implementa caché usando hash SHA-256 para evitar análisis duplicados.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Analiza una secuencia de ADN y determina si es mutante.
     * Implementa caché: si el ADN ya fue analizado, retorna el resultado guardado.
     *
     * @param dna Array de strings que representa la matriz NxN de ADN
     * @return true si es mutante, false si no lo es
     */
    @Transactional
    public boolean analyzeDna(String[] dna) {
        // Calcular hash del ADN para deduplicación
        String dnaHash = calculateDnaHash(dna);
        log.debug("DNA hash calculado: {}", dnaHash);

        // Buscar en BD si ya fue analizado (CACHÉ)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);

        if (existingRecord.isPresent()) {
            log.info("DNA ya analizado previamente (caché hit). Hash: {}", dnaHash);
            return existingRecord.get().isMutant();
        }

        // Si no existe, analizar con el algoritmo
        log.debug("Analizando nueva secuencia de ADN...");
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar resultado en BD
        DnaRecord record = new DnaRecord(dnaHash, isMutant);
        dnaRecordRepository.save(record);
        log.info("Resultado guardado - Mutante: {}, Hash: {}", isMutant, dnaHash);

        return isMutant;
    }

    /**
     * Calcula el hash SHA-256 de una secuencia de ADN.
     * Se usa para deduplicación en la base de datos.
     *
     * @param dna Array de strings del ADN
     * @return Hash SHA-256 en formato hexadecimal
     * @throws DnaHashCalculationException si hay error en el cálculo
     */
    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // Convertir bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error calculando hash SHA-256", e);
            throw new DnaHashCalculationException("Error calculando hash del ADN", e);
        }
    }
}
