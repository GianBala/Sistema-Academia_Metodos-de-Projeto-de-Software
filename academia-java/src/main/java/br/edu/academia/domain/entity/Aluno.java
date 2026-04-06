package br.edu.academia.domain.entity;



public class Aluno extends Entidade {

    private final int matricula;

    private Aluno(Builder builder) {
        super(builder);
        this.matricula = builder.matricula;
    }

    public int getMatricula() { return matricula; }

    @Override
    protected String detalhesAdicionais() {
        return String.format("Matricula: %d", matricula);
    }

    public static class Builder extends BuilderBase<Builder> {
        private int matricula;

        public Builder matricula(int matricula) {
            this.matricula = matricula;
            return this;
        }

        @Override
        public Aluno build() {
            validarCamposBase();
            if (matricula <= 0) {
                throw new IllegalStateException("Matrícula é obrigatória e deve ser positiva.");
            }
            return new Aluno(this);
        }
    }
}