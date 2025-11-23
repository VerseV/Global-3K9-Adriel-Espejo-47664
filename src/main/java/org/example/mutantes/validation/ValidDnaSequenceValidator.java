package org.example.mutantes.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Pattern DNA_PATTERN = Pattern.compile("^[ATCG]+$");
    private static final int MIN_SIZE = 4;

    @Override
    public void initialize(ValidDnaSequence constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        // Validar que no sea null o vacío
        if (dna == null || dna.length == 0) {
            return false;
        }

        // Validar tamaño mínimo
        if (dna.length < MIN_SIZE) {
            return false;
        }

        final int n = dna.length;

        // Validar que sea matriz cuadrada NxN y solo contenga A, T, C, G
        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false;
            }

            if (!DNA_PATTERN.matcher(row).matches()) {
                return false;
            }
        }

        return true;
    }
}