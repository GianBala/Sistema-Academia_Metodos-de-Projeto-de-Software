package br.edu.academia.domain.memento;

import java.time.LocalDateTime;

public class CadastroMemento {

    private final long entidadeId;
    private final TipoEntidade tipoEntidade;
    private final String descricao;
    private final LocalDateTime timestamp;

    public CadastroMemento(long entidadeId, TipoEntidade tipoEntidade, String descricao) {
        this.entidadeId = entidadeId;
        this.tipoEntidade = tipoEntidade;
        this.descricao = descricao;
        this.timestamp = LocalDateTime.now();
    }

    public long getEntidadeId() {
        return entidadeId;
    }

    public TipoEntidade getTipoEntidade() {
        return tipoEntidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
