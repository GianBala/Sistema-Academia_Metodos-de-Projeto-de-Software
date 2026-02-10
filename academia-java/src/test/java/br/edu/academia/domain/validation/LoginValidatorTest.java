package br.edu.academia.domain.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginValidatorTest {

    private LoginValidator validator;

    @BeforeEach
    void setUp() {
        validator = new LoginValidator();
    }

    @Test
    void deveAceitarLoginValido() {
        assertTrue(validator.validate("admin").isValid());
    }

    @Test
    void deveRejeitarLoginVazio() {
        assertFalse(validator.validate("").isValid());
    }

    @Test
    void deveRejeitarLoginNulo() {
        assertFalse(validator.validate(null).isValid());
    }

    @Test
    void deveRejeitarLoginComNumeros() {
        assertFalse(validator.validate("admin123").isValid());
    }

    @Test
    void deveRejeitarLoginMaiorQue12Caracteres() {
        assertFalse(validator.validate("administrador").isValid());
    }

    @Test
    void deveAceitarLoginCom12Caracteres() {
        assertTrue(validator.validate("abcdefghijkl").isValid());
    }
}
