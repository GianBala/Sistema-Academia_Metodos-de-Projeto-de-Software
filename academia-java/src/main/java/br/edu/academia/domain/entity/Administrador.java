package br.edu.academia.domain.entity;

import java.time.LocalDate;

public class Administrador extends Entidade {

    private final String login;
    private final String senhaHash;

    public Administrador(String nome, LocalDate dataNascimento, String email,
                         String login, String senhaHash) {
        super(nome, dataNascimento, email);
        this.login = login;
        this.senhaHash = senhaHash;
    }

    public String getLogin() {
        return login;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    @Override
    public String infos() {
        return super.infos() + String.format(" | Login: %s", login);
    }
}
