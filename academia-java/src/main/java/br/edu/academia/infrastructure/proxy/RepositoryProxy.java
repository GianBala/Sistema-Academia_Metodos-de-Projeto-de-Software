package br.edu.academia.infrastructure.proxy;

import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.Repository;
import br.edu.academia.domain.service.SessionContext;

import java.util.List;
import java.util.Optional;

/**
 * Padrão Proxy — controle de acesso para operações de repositório.
 *
 * Verifica as permissões do usuário logado (via SessionContext) antes de
 * delegar operações de escrita ao repositório real.
 *
 * Regras de permissão:
 * - ADMINISTRADOR: tudo permitido
 * - ATENDENTE: CRUD em ALUNO e PROFESSOR
 * - PROFESSOR/ALUNO: somente leitura
 */
public class RepositoryProxy<T> implements Repository<T> {

    protected final Repository<T> delegate;
    protected final TipoEntidade tipoProtegido;

    public RepositoryProxy(Repository<T> delegate, TipoEntidade tipoProtegido) {
        this.delegate = delegate;
        this.tipoProtegido = tipoProtegido;
    }

    @Override
    public void save(T entity) {
        verificarPermissaoEscrita("salvar");
        delegate.save(entity);
    }

    @Override
    public void update(T entity) {
        verificarPermissaoEscrita("atualizar");
        delegate.update(entity);
    }

    @Override
    public void delete(long id) {
        verificarPermissaoEscrita("deletar");
        delegate.delete(id);
    }

    @Override
    public List<T> findAll() {
        return delegate.findAll();
    }

    @Override
    public Optional<T> findById(long id) {
        return delegate.findById(id);
    }

    protected void verificarPermissaoEscrita(String operacao) {
        TipoEntidade role = SessionContext.getCurrentRole();

        if (role == null) {
            // Antes do login, permite operações do seed (criação do admin padrão)
            return;
        }
        if (role == TipoEntidade.ADMINISTRADOR) {
            return;
        }
        if (role == TipoEntidade.ATENDENTE) {
            if (tipoProtegido == TipoEntidade.ALUNO || tipoProtegido == TipoEntidade.PROFESSOR) {
                return;
            }
            throw new SecurityException(
                    "Atendente nao pode " + operacao + " " + tipoProtegido);
        }
        throw new SecurityException(
                role + " nao tem permissao para " + operacao + " " + tipoProtegido);
    }
}
