package br.edu.academia.domain.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenhaValidatorTest {

    @Test
    void deveAceitarSenhaForte() {
        SenhaValidator validator = new SenhaValidator("usuario", "user@email.com");
        assertTrue(validator.validate("Abc12345!").isValid());
    }

    @Test
    void deveRejeitarSenhaCurta() {
        SenhaValidator validator = new SenhaValidator("user", "user@e.com");
        assertFalse(validator.validate("Ab1!").isValid());
    }

    @Test
    void deveRejeitarSenhaSemTiposSuficientes() {
        SenhaValidator validator = new SenhaValidator("user", "user@e.com");
        assertFalse(validator.validate("abcdefgh").isValid());
    }

    @Test
    void deveRejeitarSenhaIgualAoNome() {
        SenhaValidator validator = new SenhaValidator("Abc12345!", "user@e.com");
        assertFalse(validator.validate("Abc12345!").isValid());
    }

    @Test
    void deveRejeitarSenhaIgualAoEmail() {
        SenhaValidator validator = new SenhaValidator("user", "Abc12345!");
        assertFalse(validator.validate("Abc12345!").isValid());
    }

    @Test
    void deveRejeitarSenhaNula() {
        SenhaValidator validator = new SenhaValidator("user", "user@e.com");
        assertFalse(validator.validate(null).isValid());
    }

    @Test
    void deveAceitarSenhaComTresTipos() {
        SenhaValidator validator = new SenhaValidator("user", "user@e.com");
        assertTrue(validator.validate("Abcdefg1").isValid());
    }
}
