package org.example.mutantes.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mutantes.dto.StatsResponse;
import org.example.mutantes.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que proporciona estadísticas de las verificaciones de ADN.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    /**
     * Obtiene las estadísticas de verificaciones de ADN:
     * - Cantidad de mutantes
     * - Cantidad de humanos
     * - Ratio (mutantes / humanos)
     *
     * @return StatsResponse con las estadísticas
     */
    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        long countMutant = dnaRecordRepository.countByIsMutant(true);
        long countHuman = dnaRecordRepository.countByIsMutant(false);

        double ratio = calculateRatio(countMutant, countHuman);

        log.debug("Estadísticas - Mutantes: {}, Humanos: {}, Ratio: {}",
                countMutant, countHuman, ratio);

        return new StatsResponse(countMutant, countHuman, ratio);
    }

    /**
     * Calcula el ratio mutantes/humanos.
     * Casos especiales:
     * - Si no hay humanos pero hay mutantes: retorna count_mutant
     * - Si no hay ninguno: retorna 0.0
     *
     * @param countMutant Cantidad de mutantes
     * @param countHuman Cantidad de humanos
     * @return Ratio calculado
     */
    private double calculateRatio(long countMutant, long countHuman) {
        if (countHuman == 0) {
            return countMutant > 0 ? (double) countMutant : 0.0;
        }
        return (double) countMutant / countHuman;
    }
}