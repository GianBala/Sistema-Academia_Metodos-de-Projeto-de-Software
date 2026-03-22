package br.edu.academia.domain.entity;

import java.time.LocalDateTime;

public class RegistroAcesso {

    private long id;
    private final String tipoOperacao;
    private final String tipoEntidade;
    private final LocalDateTime timestamp;

    public RegistroAcesso(String tipoOperacao, String tipoEntidade, LocalDateTime timestamp) {
        this.tipoOperacao = tipoOperacao;
        this.tipoEntidade = tipoEntidade;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
