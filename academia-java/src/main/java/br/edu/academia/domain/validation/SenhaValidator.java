package br.edu.academia.domain.validation;

public class SenhaValidator implements Validator<String> {

    private final String nome;
    private final String email;

    public SenhaValidator(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    @Override
    public ValidationResult validate(String senha) {
        if (senha == null || senha.length() < 8 || senha.length() > 128) {
            return ValidationResult.error("A senha deve ter entre 8 e 128 caracteres.");
        }
        if (!temTiposSuficientes(senha)) {
            return ValidationResult.error(
                    "A senha deve conter pelo menos 3 dos seguintes: maiuscula, minuscula, numero, caractere especial.");
        }
        if (senha.equals(nome) || senha.equals(email)) {
            return ValidationResult.error("A senha nao pode ser igual ao nome ou email.");
        }
        return ValidationResult.ok();
    }

    private boolean temTiposSuficientes(String senha) {
        boolean maiuscula = false;
        boolean minuscula = false;
        boolean digito = false;
        boolean especial = false;

        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) maiuscula = true;
            else if (Character.isLowerCase(c)) minuscula = true;
            else if (Character.isDigit(c)) digito = true;
            else especial = true;
        }

        int tipos = (maiuscula ? 1 : 0) + (minuscula ? 1 : 0)
                + (digito ? 1 : 0) + (especial ? 1 : 0);
        return tipos >= 3;
    }
}
