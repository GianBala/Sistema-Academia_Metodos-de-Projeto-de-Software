package br.edu.academia.domain.entity;

public class Atendente extends Funcionario {

    private Atendente(Builder builder) {
        super(builder);
    }

    @Override
    protected String detalhesAdicionais() {
        return "";
    }

    public static class Builder extends BuilderBase<Builder> {
        @Override
        public Atendente build() {
            validarCamposBase();
            return new Atendente(this);
        }
    }
}