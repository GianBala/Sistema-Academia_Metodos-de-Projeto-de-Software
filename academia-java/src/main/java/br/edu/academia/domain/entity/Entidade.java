package br.edu.academia.domain.entity;

import java.time.LocalDate;
import java.time.Period;

/**
 * Entidade base de todos os usuários do sistema.
 * Usa o padrão Builder para construção (BuilderBase com CRTP).
 * O método infos() implementa o padrão Template Method.
 */
public abstract class Entidade {

    private long id;
    private final String nome;
    private final LocalDate dataNascimento;
    private final String email;
    private final String login;
    private final String senhaHash;

    protected Entidade(BuilderBase<?> builder) {
        this.nome = formatarNome(builder.nome);
        this.dataNascimento = builder.dataNascimento;
        this.email = builder.email;
        this.login = builder.login;
        this.senhaHash = builder.senhaHash;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getEmail() { return email; }
    public String getLogin() { return login; }
    public String getSenhaHash() { return senhaHash; }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    /**
     * Template Method: define o esqueleto para exibição de informações.
     * Subclasses implementam detalhesAdicionais() com seus dados específicos.
     */
    public final String infos() {
        String base = String.format("Nome: %s | Idade: %d | Email: %s | Login: %s | Cargo: %s",
                nome, getIdade(), email, login, getCargo());
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

    /**
     * Builder base compartilhado — padrão Builder com CRTP para method chaining fluente.
     */
    public abstract static class BuilderBase<T extends BuilderBase<T>> {
        private String nome;
        private LocalDate dataNascimento;
        private String email;
        private String login;
        private String senhaHash;

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

        @SuppressWarnings("unchecked")
        public T login(String login) {
            this.login = login;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T senhaHash(String senhaHash) {
            this.senhaHash = senhaHash;
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
            if (login == null || login.isBlank()) {
                throw new IllegalStateException("Login é obrigatório.");
            }
            if (senhaHash == null || senhaHash.isBlank()) {
                throw new IllegalStateException("Senha hash é obrigatória.");
            }
        }

        public abstract Entidade build();
    }
}
