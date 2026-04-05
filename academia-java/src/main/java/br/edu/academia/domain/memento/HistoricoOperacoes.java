package br.edu.academia.domain.memento;

import br.edu.academia.domain.repository.Repository;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;

public class HistoricoOperacoes {

    private final Deque<CadastroMemento> historico = new ArrayDeque<>();
    private final Map<TipoEntidade, Repository<?>> repositorios;

    public HistoricoOperacoes(Map<TipoEntidade, Repository<?>> repositorios) {
        this.repositorios = repositorios;
    }

    public void registrar(CadastroMemento memento) {
        historico.push(memento);
    }

    public Optional<CadastroMemento> desfazer() {
        if (historico.isEmpty()) {
            return Optional.empty();
        }

        CadastroMemento memento = historico.pop();
        Repository<?> repository = repositorios.get(memento.getTipoEntidade());

        if (repository == null) {
            throw new IllegalStateException(
                    "Repositorio nao encontrado para tipo: " + memento.getTipoEntidade());
        }

        repository.delete(memento.getEntidadeId());
        return Optional.of(memento);
    }

    public boolean temHistorico() {
        return !historico.isEmpty();
    }
}
