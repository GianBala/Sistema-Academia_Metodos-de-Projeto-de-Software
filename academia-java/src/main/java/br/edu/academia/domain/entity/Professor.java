package br.edu.academia.domain.entity;

/**
 * Professor do sistema.
 * Pertence a uma ou mais Turmas (gerenciadas via TurmaService/DB).
 * login/senhaHash herdados de Entidade.
 */
public class Professor extends Funcionario {

    private Professor(Builder builder) {
        super(builder);
    }

    @Override
    protected String detalhesAdicionais() {
        return "";
    }

    public static class Builder extends BuilderBase<Builder> {
        @Override
        public Professor build() {
            validarCamposBase();
            return new Professor(this);
        }
    }
}
