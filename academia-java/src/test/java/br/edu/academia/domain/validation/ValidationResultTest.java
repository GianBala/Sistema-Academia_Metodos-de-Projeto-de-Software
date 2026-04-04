package br.edu.academia.domain.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    void okDeveSerValido() {
        ValidationResult result = ValidationResult.ok();

        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void errorDeveSerInvalido() {
        ValidationResult result = ValidationResult.error("Erro teste");

        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        assertEquals("Erro teste", result.getErrors().get(0));
    }

    @Test
    void mergeDeveAgruparErros() {
        ValidationResult r1 = ValidationResult.error("Erro 1");
        ValidationResult r2 = ValidationResult.error("Erro 2");
        ValidationResult r3 = ValidationResult.ok();

        ValidationResult merged = ValidationResult.merge(List.of(r1, r2, r3));

        assertFalse(merged.isValid());
        assertEquals(2, merged.getErrors().size());
    }

    @Test
    void mergeDeApenasSucessosDeveSerValido() {
        ValidationResult merged = ValidationResult.merge(
                List.of(ValidationResult.ok(), ValidationResult.ok()));

        assertTrue(merged.isValid());
    }

    @Test
    void getErrorMessageDeveJuntarErrosComPontoEVirgula() {
        ValidationResult result = ValidationResult.merge(List.of(
                ValidationResult.error("Erro 1"),
                ValidationResult.error("Erro 2")
        ));

        assertEquals("Erro 1; Erro 2", result.getErrorMessage());
    }
}
