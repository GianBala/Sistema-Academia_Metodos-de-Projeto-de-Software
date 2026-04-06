package br.edu.academia.domain.entity;



public class Administrador extends Entidade {

    private final String login;
    private final String senhaHash;

    private Administrador(Builder builder) {
        super(builder);
        this.login = builder.login;
        this.senhaHash = builder.senhaHash;
    }

    public String getLogin() { return login; }
    public String getSenhaHash() { return senhaHash; }

    @Override
    public String infos() {
        return super.infos() + String.format(" | Login: %s", login);
    }

    public static class Builder extends BuilderBase<Builder> {
        private String login;
        private String senhaHash;

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder senhaHash(String senhaHash) {
            this.senhaHash = senhaHash;
            return this;
        }

        @Override
        public Administrador build() {
            validarCamposBase();
            if (login == null || login.isBlank()) {
                throw new IllegalStateException("Login é obrigatório.");
            }
            if (senhaHash == null || senhaHash.isBlank()) {
                throw new IllegalStateException("Senha hash é obrigatória.");
            }
            return new Administrador(this);
        }
    }
}