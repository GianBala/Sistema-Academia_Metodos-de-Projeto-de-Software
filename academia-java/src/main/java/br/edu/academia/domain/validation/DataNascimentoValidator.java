package br.edu.academia.domain.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataNascimentoValidator implements Validator<String> {

    private static final DateTimeFormatter FORMATO_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public ValidationResult validate(String data) {
        LocalDate parsed = parse(data);
        if (parsed == null) {
            return ValidationResult.error(
                    "Formato de data invalido: '" + data + "'. Use dd/mm/aaaa ou aaaa-mm-dd.");
        }
        if (parsed.isAfter(LocalDate.now())) {
            return ValidationResult.error("Data de nascimento nao pode ser no futuro.");
        }
        return ValidationResult.ok();
    }

    public LocalDate parse(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }
        LocalDate parsed = tryParse(data, FORMATO_BR);
        if (parsed == null) {
            parsed = tryParse(data, FORMATO_ISO);
        }
        return parsed;
    }

    private LocalDate tryParse(String data, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
