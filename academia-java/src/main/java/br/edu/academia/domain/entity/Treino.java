package br.edu.academia.domain.entity;

import java.time.LocalDate;

/**
 * Treino — exercício prescrito por um professor para um aluno.
 * Contém grupo muscular, exercício, repetições e peso.
 * Usa o padrão Builder para construção.
 */
public class Treino {

    private long id;
    private final long alunoId;
    private final String grupoMuscular;
    private final String exercicio;
    private final int repeticoes;
    private final double peso;
    private final LocalDate data;

    private Treino(Builder builder) {
        this.alunoId = builder.alunoId;
        this.grupoMuscular = builder.grupoMuscular;
        this.exercicio = builder.exercicio;
        this.repeticoes = builder.repeticoes;
        this.peso = builder.peso;
        this.data = builder.data;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getAlunoId() { return alunoId; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public String getExercicio() { return exercicio; }
    public int getRepeticoes() { return repeticoes; }
    public double getPeso() { return peso; }
    public LocalDate getData() { return data; }

    public String infos() {
        return String.format(
                "Treino [%d] | Grupo: %s | Exercício: %s | Séries: %dx | Peso: %.1f kg | Data: %s",
                id, grupoMuscular, exercicio, repeticoes, peso, data);
    }

    public static class Builder {
        private long alunoId;
        private String grupoMuscular;
        private String exercicio;
        private int repeticoes;
        private double peso;
        private LocalDate data;

        public Builder alunoId(long alunoId) {
            this.alunoId = alunoId;
            return this;
        }

        public Builder grupoMuscular(String grupoMuscular) {
            this.grupoMuscular = grupoMuscular;
            return this;
        }

        public Builder exercicio(String exercicio) {
            this.exercicio = exercicio;
            return this;
        }

        public Builder repeticoes(int repeticoes) {
            this.repeticoes = repeticoes;
            return this;
        }

        public Builder peso(double peso) {
            this.peso = peso;
            return this;
        }

        public Builder data(LocalDate data) {
            this.data = data;
            return this;
        }

        public Treino build() {
            if (alunoId <= 0) throw new IllegalStateException("Aluno é obrigatório.");
            if (grupoMuscular == null || grupoMuscular.isBlank()) throw new IllegalStateException("Grupo muscular é obrigatório.");
            if (exercicio == null || exercicio.isBlank()) throw new IllegalStateException("Exercício é obrigatório.");
            if (repeticoes <= 0) throw new IllegalStateException("Repetições devem ser positivas.");
            if (peso < 0) throw new IllegalStateException("Peso não pode ser negativo.");
            if (data == null) throw new IllegalStateException("Data é obrigatória.");
            return new Treino(this);
        }
    }
}
