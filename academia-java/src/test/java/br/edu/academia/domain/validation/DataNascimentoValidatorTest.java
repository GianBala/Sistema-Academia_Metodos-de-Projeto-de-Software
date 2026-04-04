package br.edu.academia.domain.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DataNascimentoValidatorTest {

    private DataNascimentoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DataNascimentoValidator();
    }

    @Test
    void deveAceitarFormatoBrasileiro() {
        assertTrue(validator.validate("15/03/2000").isValid());
    }

    @Test
    void deveAceitarFormatoISO() {
        assertTrue(validator.validate("2000-03-15").isValid());
    }

    @Test
    void deveRejeitarFormatoInvalido() {
        assertFalse(validator.validate("abc").isValid());
    }

    @Test
    void deveRejeitarDataFutura() {
        String dataFutura = LocalDate.now().plusDays(1).toString();
        assertFalse(validator.validate(dataFutura).isValid());
    }

    @Test
    void deveRejeitarNulo() {
        assertFalse(validator.validate(null).isValid());
    }

    @Test
    void deveRejeitarVazio() {
        assertFalse(validator.validate("").isValid());
    }

    @Test
    void deveParsearFormatoBrasileiro() {
        LocalDate date = validator.parse("15/03/2000");
        assertEquals(LocalDate.of(2000, 3, 15), date);
    }

    @Test
    void deveParsearFormatoISO() {
        LocalDate date = validator.parse("2000-03-15");
        assertEquals(LocalDate.of(2000, 3, 15), date);
    }
}
