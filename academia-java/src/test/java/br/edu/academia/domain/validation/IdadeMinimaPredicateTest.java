package br.edu.academia.domain.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IdadeMinimaPredicateTest {

    @Test
    void deveAceitarIdadeIgualAoMinimo() {
        IdadeMinimaPredicate validator = new IdadeMinimaPredicate(18);
        LocalDate nascimento = LocalDate.now().minusYears(18);

        assertTrue(validator.validate(nascimento).isValid());
    }

    @Test
    void deveAceitarIdadeMaiorQueMinimo() {
        IdadeMinimaPredicate validator = new IdadeMinimaPredicate(18);
        LocalDate nascimento = LocalDate.now().minusYears(25);

        assertTrue(validator.validate(nascimento).isValid());
    }

    @Test
    void deveRejeitarIdadeMenorQueMinimo() {
        IdadeMinimaPredicate validator = new IdadeMinimaPredicate(18);
        LocalDate nascimento = LocalDate.now().minusYears(17);

        assertFalse(validator.validate(nascimento).isValid());
    }

    @Test
    void devePermitirIdadeMinimaCustomizada() {
        IdadeMinimaPredicate validator = new IdadeMinimaPredicate(21);
        LocalDate nascimento = LocalDate.now().minusYears(20);

        assertFalse(validator.validate(nascimento).isValid());
    }
}
