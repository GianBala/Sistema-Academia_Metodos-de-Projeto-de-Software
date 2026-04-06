package br.edu.academia.domain.entity;

import java.time.LocalDate;
import java.time.Period;

public abstract class Entidade {

    private long id;
    private final String nome;
    private final LocalDate dataNascimento;
    private final String email;

    protected Entidade(BuilderBase<?> builder) {
        this.nome = formatarNome(builder.nome);
        this.dataNascimento = builder.dataNascimento;
        this.email = builder.email;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getEmail() { return email; }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public final String infos() {
        String base = String.format("Nome: %s | Idade: %d | Email: %s | Cargo: %s",
                nome, getIdade(), email, getCargo());
        String detalhes = detalhesAdicionais();
        if (detalhes != null && !detalhes.isEmpty()) {
            return base + " | " + detalhes;
        }
        return base;
    }

    protected abstract String detalhesAdicionais();

    protected String getCargo() {
        return getClass().getSimpleName();
    }

    private String formatarNome(String nome) {
        if (nome == null || nome.isBlank()) return nome;
        String[] palavras = nome.trim().toLowerCase().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palavra : palavras) {
            if (!resultado.isEmpty()) resultado.append(" ");
            resultado.append(Character.toUpperCase(palavra.charAt(0)));
            resultado.append(palavra.substring(1));
        }
        return resultado.toString();
    }

    // Builder base — compartilha os campos comuns a todas as entidades
    public abstract static class BuilderBase<T extends BuilderBase<T>> {
        private String nome;
        private LocalDate dataNascimento;
        private String email;

        @SuppressWarnings("unchecked")
        public T nome(String nome) {
            this.nome = nome;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T dataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = dataNascimento;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T email(String email) {
            this.email = email;
            return (T) this;
        }

        protected void validarCamposBase() {
            if (nome == null || nome.isBlank()) {
                throw new IllegalStateException("Nome é obrigatório.");
            }
            if (dataNascimento == null) {
                throw new IllegalStateException("Data de nascimento é obrigatória.");
            }
            if (email == null || email.isBlank()) {
                throw new IllegalStateException("Email é obrigatório.");
            }
        }

        public abstract Entidade build();
    }
}