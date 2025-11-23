package org.example.mutantes.repository;

import org.example.mutantes.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    /**
     * Busca un registro de ADN por su hash SHA-256
     * @param dnaHash Hash del ADN
     * @return Optional con el registro si existe
     */
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    /**
     * Cuenta cuántos registros de ADN son mutantes o humanos
     * @param isMutant true para mutantes, false para humanos
     * @return Cantidad de registros
     */
    long countByIsMutant(boolean isMutant);
}
