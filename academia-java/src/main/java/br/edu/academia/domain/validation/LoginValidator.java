package br.edu.academia.domain.validation;

public class LoginValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String login) {
        if (login == null || login.trim().isEmpty()) {
            return ValidationResult.error("O login nao pode ser vazio.");
        }
        if (login.chars().anyMatch(Character::isDigit)) {
            return ValidationResult.error("O login nao pode conter numeros.");
        }
        if (login.length() > 12) {
            return ValidationResult.error("O login deve ter no maximo 12 caracteres.");
        }
        return ValidationResult.ok();
    }
}
