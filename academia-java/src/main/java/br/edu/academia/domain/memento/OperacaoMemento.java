package br.edu.academia.domain.memento;

import java.time.LocalDateTime;

/**
 * Memento que armazena uma operação com sua ação de desfazer (undo).
 * Usa Runnable para encapsular a lógica de desfazer sem depender de tipos concretos.
 *
 * Padrão Memento — suporta desfazer tanto CRIACAO (deletar entidade)
 * quanto ATUALIZACAO (restaurar estado anterior).
 */
public class OperacaoMemento {

    private final TipoOperacao tipoOperacao;
    private final String descricao;
    private final LocalDateTime timestamp;
    private final Runnable acaoDesfazer;

    public OperacaoMemento(TipoOperacao tipoOperacao, String descricao, Runnable acaoDesfazer) {
        this.tipoOperacao = tipoOperacao;
        this.descricao = descricao;
        this.timestamp = LocalDateTime.now();
        this.acaoDesfazer = acaoDesfazer;
    }

    public void desfazer() {
        acaoDesfazer.run();
    }

    public TipoOperacao getTipoOperacao() { return tipoOperacao; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
