package br.edu.academia.infrastructure.proxy;

import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.AtendenteRepository;

import java.util.Optional;

public class AtendenteRepositoryProxy extends RepositoryProxy<Atendente> implements AtendenteRepository {

    private final AtendenteRepository atendenteDelegate;

    public AtendenteRepositoryProxy(AtendenteRepository delegate) {
        super(delegate, TipoEntidade.ATENDENTE);
        this.atendenteDelegate = delegate;
    }

    @Override
    public Optional<Atendente> findByLogin(String login) {
        return atendenteDelegate.findByLogin(login);
    }
}
