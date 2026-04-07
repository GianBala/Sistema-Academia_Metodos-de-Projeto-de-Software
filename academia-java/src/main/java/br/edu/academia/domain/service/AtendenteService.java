package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.repository.AtendenteRepository;
import br.edu.academia.domain.validation.*;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AtendenteService {

    private static final int IDADE_MINIMA_FUNCIONARIO = 18;

    private final AtendenteRepository repository;
    private final PasswordHasher passwordHasher;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final LoginValidator loginValidator;
    private final IdadeMinimaPredicate idadeValidator;
    private final HistoricoOperacoes historico;
    private final Logger logger;

    public AtendenteService(AtendenteRepository repository,
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

    public Atendente cadastrar(String nome, String dataNascimentoStr, String email,
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

        Atendente atendente = new Atendente.Builder()
                .nome(nome).dataNascimento(dataNascimento).email(email)
                .login(login).senhaHash(senhaHash).build();
        repository.save(atendente);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO,
                "Cadastro de atendente: " + atendente.getNome(),
                () -> repository.delete(atendente.getId())
        ));

        logger.info("Atendente cadastrado: " + atendente.getNome());
        return atendente;
    }

    public Atendente atualizar(long id, String nome, String dataNascimentoStr, String email,
                               String login, String senha) {
        Atendente anterior = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atendente não encontrado: id=" + id));

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

        Atendente atualizado = new Atendente.Builder()
                .nome(nome).dataNascimento(dataNascimento).email(email)
                .login(login).senhaHash(senhaHash).build();
        atualizado.setId(id);
        repository.update(atualizado);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.ATUALIZACAO,
                "Atualizacao de atendente: " + anterior.getNome(),
                () -> repository.update(anterior)
        ));

        logger.info("Atendente atualizado: id=" + id);
        return atualizado;
    }

    public void deletar(long id) {
        Atendente atendente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Atendente não encontrado: id=" + id));
        repository.delete(id);
        logger.info("Atendente deletado: " + atendente.getNome());
    }

    public Optional<Atendente> buscarPorId(long id) { return repository.findById(id); }
    public List<Atendente> listarTodos() { return repository.findAll(); }
}
