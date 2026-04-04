package br.edu.academia.domain.validation;

@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T value);
}
