package br.edu.academia.domain.validation;

import java.time.LocalDate;
import java.time.Period;

public class IdadeMinimaPredicate implements Validator<LocalDate> {

    private final int idadeMinima;

    public IdadeMinimaPredicate(int idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    @Override
    public ValidationResult validate(LocalDate dataNascimento) {
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        if (idade < idadeMinima) {
            return ValidationResult.error(
                    String.format("Funcionarios devem ter pelo menos %d anos. Idade atual: %d.",
                            idadeMinima, idade));
        }
        return ValidationResult.ok();
    }
}
