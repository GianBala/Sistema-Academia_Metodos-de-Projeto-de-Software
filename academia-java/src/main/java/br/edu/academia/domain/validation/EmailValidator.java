package br.edu.academia.domain.validation;

public class EmailValidator implements Validator<String> {

    @Override
    public ValidationResult validate(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return ValidationResult.error(
                    String.format("O email '%s' e invalido. Deve conter '@' e '.'.", email));
        }
        return ValidationResult.ok();
    }
}
