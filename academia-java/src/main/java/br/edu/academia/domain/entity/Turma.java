package br.edu.academia.domain.entity;

/**
 * Turma — agrupa alunos sob a responsabilidade de um professor.
 * Usa o padrão Builder para construção.
 */
public class Turma {

    private long id;
    private final String nome;
    private final long professorId;
    private final String horario;

    private Turma(Builder builder) {
        this.nome = builder.nome;
        this.professorId = builder.professorId;
        this.horario = builder.horario;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public long getProfessorId() { return professorId; }
    public String getHorario() { return horario; }

    public String infos() {
        return String.format("Turma [%d]: %s | Professor ID: %d | Horário: %s",
                id, nome, professorId, horario);
    }

    public static class Builder {
        private String nome;
        private long professorId;
        private String horario;

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder professorId(long professorId) {
            this.professorId = professorId;
            return this;
        }

        public Builder horario(String horario) {
            this.horario = horario;
            return this;
        }

        public Turma build() {
            if (nome == null || nome.isBlank()) {
                throw new IllegalStateException("Nome da turma é obrigatório.");
            }
            if (professorId <= 0) {
                throw new IllegalStateException("Professor é obrigatório.");
            }
            if (horario == null || horario.isBlank()) {
                throw new IllegalStateException("Horário da turma é obrigatório.");
            }
            return new Turma(this);
        }
    }
}
