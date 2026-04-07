package br.edu.academia.infrastructure.proxy;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.AlunoRepository;

import java.util.Optional;

/**
 * Proxy específico para AlunoRepository — delega métodos extras e aplica controle de acesso.
 */
public class AlunoRepositoryProxy extends RepositoryProxy<Aluno> implements AlunoRepository {

    private final AlunoRepository alunoDelegate;

    public AlunoRepositoryProxy(AlunoRepository delegate) {
        super(delegate, TipoEntidade.ALUNO);
        this.alunoDelegate = delegate;
    }

    @Override
    public Optional<Aluno> findByMatricula(int matricula) {
        return alunoDelegate.findByMatricula(matricula);
    }

    @Override
    public Optional<Aluno> findByLogin(String login) {
        return alunoDelegate.findByLogin(login);
    }
}
