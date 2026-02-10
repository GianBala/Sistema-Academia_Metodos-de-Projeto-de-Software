package br.edu.academia.domain.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
    }

    @Test
    void deveAceitarEmailValido() {
        assertTrue(validator.validate("teste@email.com").isValid());
    }

    @Test
    void deveRejeitarEmailSemArroba() {
        assertFalse(validator.validate("testeemail.com").isValid());
    }

    @Test
    void deveRejeitarEmailSemPonto() {
        assertFalse(validator.validate("teste@emailcom").isValid());
    }

    @Test
    void deveRejeitarEmailNulo() {
        assertFalse(validator.validate(null).isValid());
    }
}
