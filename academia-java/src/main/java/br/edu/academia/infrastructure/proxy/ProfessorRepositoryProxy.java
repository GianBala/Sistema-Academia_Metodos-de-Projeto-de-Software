package br.edu.academia.infrastructure.proxy;

import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.ProfessorRepository;

import java.util.Optional;

public class ProfessorRepositoryProxy extends RepositoryProxy<Professor> implements ProfessorRepository {

    private final ProfessorRepository professorDelegate;

    public ProfessorRepositoryProxy(ProfessorRepository delegate) {
        super(delegate, TipoEntidade.PROFESSOR);
        this.professorDelegate = delegate;
    }

    @Override
    public Optional<Professor> findByLogin(String login) {
        return professorDelegate.findByLogin(login);
    }
}
