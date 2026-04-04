package br.edu.academia.domain.entity;

public class Atendente extends Funcionario {

    private Atendente(Builder builder) {
        super(builder);
    }

    public static class Builder extends BuilderBase<Builder> {
        @Override
        public Atendente build() {
            return new Atendente(this);
        }
    }
}