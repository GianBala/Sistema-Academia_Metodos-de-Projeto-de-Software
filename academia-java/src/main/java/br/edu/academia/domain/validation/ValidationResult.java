package br.edu.academia.domain.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private final List<String> errors;

    private ValidationResult(List<String> errors) {
        this.errors = errors;
    }

    public static ValidationResult ok() {
        return new ValidationResult(Collections.emptyList());
    }

    public static ValidationResult error(String message) {
        return new ValidationResult(List.of(message));
    }

    public static ValidationResult merge(List<ValidationResult> results) {
        List<String> allErrors = new ArrayList<>();
        for (ValidationResult result : results) {
            allErrors.addAll(result.getErrors());
        }
        return new ValidationResult(allErrors);
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public String getErrorMessage() {
        return String.join("; ", errors);
    }
}
