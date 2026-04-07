package br.edu.academia.domain.entity;

/**
 * Administrador do sistema — acesso total a todas as operações.
 * login/senhaHash herdados de Entidade.
 */
public class Administrador extends Entidade {

    private Administrador(Builder builder) {
        super(builder);
    }

    @Override
    protected String detalhesAdicionais() {
        return null; // login já exibido no infos() base
    }

    public static class Builder extends BuilderBase<Builder> {

        @Override
        public Administrador build() {
            validarCamposBase();
            return new Administrador(this);
        }
    }
}
