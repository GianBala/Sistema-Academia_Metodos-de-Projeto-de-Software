package br.edu.academia.infrastructure.proxy;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.AdministradorRepository;

import java.util.Optional;

public class AdministradorRepositoryProxy extends RepositoryProxy<Administrador> implements AdministradorRepository {

    private final AdministradorRepository adminDelegate;

    public AdministradorRepositoryProxy(AdministradorRepository delegate) {
        super(delegate, TipoEntidade.ADMINISTRADOR);
        this.adminDelegate = delegate;
    }

    @Override
    public Optional<Administrador> findByLogin(String login) {
        return adminDelegate.findByLogin(login);
    }
}
