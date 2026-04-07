package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.repository.AdministradorRepository;
import br.edu.academia.domain.validation.*;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdministradorService {

    private final AdministradorRepository repository;
    private final PasswordHasher passwordHasher;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final LoginValidator loginValidator;
    private final HistoricoOperacoes historico;
    private final Logger logger;

    public AdministradorService(AdministradorRepository repository,
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
    }

    public Administrador cadastrar(String nome, String dataNascimentoStr, String email,
                                    String login, String senha) {
        ValidationResult dateResult = dataValidator.validate(dataNascimentoStr);
        if (!dateResult.isValid()) throw new IllegalArgumentException(dateResult.getErrorMessage());

        ValidationResult result = ValidationResult.merge(List.of(
                emailValidator.validate(email),
                loginValidator.validate(login),
                new SenhaValidator(nome, email).validate(senha)
        ));
        if (!result.isValid()) throw new IllegalArgumentException(result.getErrorMessage());

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);
        String senhaHash = passwordHasher.hash(senha);

        Administrador admin = new Administrador.Builder()
                .nome(nome).dataNascimento(dataNascimento).email(email)
                .login(login).senhaHash(senhaHash).build();
        repository.save(admin);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO,
                "Cadastro de administrador: " + admin.getNome(),
                () -> repository.delete(admin.getId())
        ));

        logger.info("Administrador cadastrado: " + admin.getNome());
        return admin;
    }

    public Administrador atualizar(long id, String nome, String dataNascimentoStr, String email,
                                    String login, String senha) {
        Administrador anterior = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado: id=" + id));

        ValidationResult dateResult = dataValidator.validate(dataNascimentoStr);
        if (!dateResult.isValid()) throw new IllegalArgumentException(dateResult.getErrorMessage());

        ValidationResult result = ValidationResult.merge(List.of(
                emailValidator.validate(email),
                loginValidator.validate(login),
                new SenhaValidator(nome, email).validate(senha)
        ));
        if (!result.isValid()) throw new IllegalArgumentException(result.getErrorMessage());

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);
        String senhaHash = passwordHasher.hash(senha);

        Administrador atualizado = new Administrador.Builder()
                .nome(nome).dataNascimento(dataNascimento).email(email)
                .login(login).senhaHash(senhaHash).build();
        atualizado.setId(id);
        repository.update(atualizado);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.ATUALIZACAO,
                "Atualizacao de administrador: " + anterior.getNome(),
                () -> repository.update(anterior)
        ));

        logger.info("Administrador atualizado: id=" + id);
        return atualizado;
    }

    public void deletar(long id) {
        Administrador admin = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado: id=" + id));
        repository.delete(id);
        logger.info("Administrador deletado: " + admin.getNome());
    }

    public Optional<Administrador> buscarPorId(long id) { return repository.findById(id); }
    public List<Administrador> listarTodos() { return repository.findAll(); }
}
