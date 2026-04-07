package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.repository.ProfessorRepository;
import br.edu.academia.domain.validation.*;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ProfessorService {

    private static final int IDADE_MINIMA_FUNCIONARIO = 18;

    private final ProfessorRepository repository;
    private final PasswordHasher passwordHasher;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final LoginValidator loginValidator;
    private final IdadeMinimaPredicate idadeValidator;
    private final HistoricoOperacoes historico;
    private final Logger logger;

    public ProfessorService(ProfessorRepository repository,
                             PasswordHasher passwordHasher,
                             HistoricoOperacoes historico,
                             Logger logger) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.historico = historico;
        this.logger = logger;
        this.dataValidator = new DataNascimentoValidator();
        this.emailValidator = new EmailValidator();
        this.loginValidator = new LoginValidator();
        this.idadeValidator = new IdadeMinimaPredicate(IDADE_MINIMA_FUNCIONARIO);
    }

    public Professor cadastrar(String nome, String dataNascimentoStr, String email,
                               String login, String senha) {
        ValidationResult dateResult = dataValidator.validate(dataNascimentoStr);
        if (!dateResult.isValid()) throw new IllegalArgumentException(dateResult.getErrorMessage());

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);

        ValidationResult result = ValidationResult.merge(List.of(
                emailValidator.validate(email),
                loginValidator.validate(login),
                idadeValidator.validate(dataNascimento),
                new SenhaValidator(nome, email).validate(senha)
        ));
        if (!result.isValid()) throw new IllegalArgumentException(result.getErrorMessage());

        String senhaHash = passwordHasher.hash(senha);

        Professor professor = new Professor.Builder()
                .nome(nome)
                .dataNascimento(dataNascimento)
                .email(email)
                .login(login)
                .senhaHash(senhaHash)
                .build();
        repository.save(professor);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO,
                "Cadastro de professor: " + professor.getNome(),
                () -> repository.delete(professor.getId())
        ));

        logger.info("Professor cadastrado: " + professor.getNome());
        return professor;
    }

    public Professor atualizar(long id, String nome, String dataNascimentoStr, String email,
                               String login, String senha) {
        Professor anterior = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado: id=" + id));

        ValidationResult dateResult = dataValidator.validate(dataNascimentoStr);
        if (!dateResult.isValid()) throw new IllegalArgumentException(dateResult.getErrorMessage());

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);

        ValidationResult result = ValidationResult.merge(List.of(
                emailValidator.validate(email),
                loginValidator.validate(login),
                idadeValidator.validate(dataNascimento),
                new SenhaValidator(nome, email).validate(senha)
        ));
        if (!result.isValid()) throw new IllegalArgumentException(result.getErrorMessage());

        String senhaHash = passwordHasher.hash(senha);

        Professor atualizado = new Professor.Builder()
                .nome(nome).dataNascimento(dataNascimento).email(email)
                .login(login).senhaHash(senhaHash).build();
        atualizado.setId(id);
        repository.update(atualizado);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.ATUALIZACAO,
                "Atualizacao de professor: " + anterior.getNome(),
                () -> repository.update(anterior)
        ));

        logger.info("Professor atualizado: id=" + id);
        return atualizado;
    }

    public void deletar(long id) {
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado: id=" + id));
        repository.delete(id);
        logger.info("Professor deletado: " + professor.getNome());
    }

    public Optional<Professor> buscarPorId(long id) { return repository.findById(id); }
    public List<Professor> listarTodos() { return repository.findAll(); }
}
