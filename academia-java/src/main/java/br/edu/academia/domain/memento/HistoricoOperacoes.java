package br.edu.academia.domain.memento;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Caretaker do padrão Memento.
 * Mantém uma pilha de mementos e executa desfazer (undo).
 * Suporta desfazer criação e atualização.
 */
public class HistoricoOperacoes {

    private final Deque<OperacaoMemento> historico = new ArrayDeque<>();

    public void registrar(OperacaoMemento memento) {
        historico.push(memento);
    }

    public Optional<OperacaoMemento> desfazer() {
        if (historico.isEmpty()) {
            return Optional.empty();
        }
        OperacaoMemento memento = historico.pop();
        memento.desfazer();
        return Optional.of(memento);
    }

    public boolean temHistorico() {
        return !historico.isEmpty();
    }

    public int tamanho() {
        return historico.size();
    }
}
