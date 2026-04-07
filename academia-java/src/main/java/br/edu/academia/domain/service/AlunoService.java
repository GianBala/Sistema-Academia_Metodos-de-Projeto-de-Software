package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.repository.AlunoRepository;
import br.edu.academia.domain.validation.*;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AlunoService {

    private final AlunoRepository repository;
    private final MatriculaGenerator matriculaGenerator;
    private final PasswordHasher passwordHasher;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final LoginValidator loginValidator;
    private final HistoricoOperacoes historico;
    private final Logger logger;

    public AlunoService(AlunoRepository repository,
                        MatriculaGenerator matriculaGenerator,
                        PasswordHasher passwordHasher,
                        HistoricoOperacoes historico,
                        Logger logger) {
        this.repository = repository;
        this.matriculaGenerator = matriculaGenerator;
        this.passwordHasher = passwordHasher;
        this.historico = historico;
        this.logger = logger;
        this.dataValidator = new DataNascimentoValidator();
        this.emailValidator = new EmailValidator();
        this.loginValidator = new LoginValidator();
    }

    public Aluno cadastrar(String nome, String dataNascimentoStr, String email,
                           String login, String senha) {
        ValidationResult result = ValidationResult.merge(List.of(
                dataValidator.validate(dataNascimentoStr),
                emailValidator.validate(email),
                loginValidator.validate(login),
                new SenhaValidator(nome, email).validate(senha)
        ));
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorMessage());
        }

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);
        int matricula = matriculaGenerator.generate();
        String senhaHash = passwordHasher.hash(senha);

        Aluno aluno = new Aluno.Builder()
                .nome(nome)
                .dataNascimento(dataNascimento)
                .email(email)
                .matricula(matricula)
                .login(login)
                .senhaHash(senhaHash)
                .build();
        repository.save(aluno);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO,
                "Cadastro de aluno: " + aluno.getNome(),
                () -> repository.delete(aluno.getId())
        ));

        logger.info("Aluno cadastrado: " + aluno.getNome() + " (matricula " + aluno.getMatricula() + ")");
        return aluno;
    }

    public Aluno atualizar(long id, String nome, String dataNascimentoStr, String email,
                           String login, String senha) {
        Aluno anterior = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: id=" + id));

        ValidationResult result = ValidationResult.merge(List.of(
                dataValidator.validate(dataNascimentoStr),
                emailValidator.validate(email),
                loginValidator.validate(login),
                new SenhaValidator(nome, email).validate(senha)
        ));
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorMessage());
        }

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);
        String senhaHash = passwordHasher.hash(senha);

        Aluno atualizado = new Aluno.Builder()
                .nome(nome)
                .dataNascimento(dataNascimento)
                .email(email)
                .matricula(anterior.getMatricula())
                .login(login)
                .senhaHash(senhaHash)
                .build();
        atualizado.setId(id);
        repository.update(atualizado);

        // Memento para desfazer atualização — salva o estado anterior
        historico.registrar(new OperacaoMemento(
                TipoOperacao.ATUALIZACAO,
                "Atualizacao de aluno: " + anterior.getNome(),
                () -> repository.update(anterior)
        ));

        logger.info("Aluno atualizado: id=" + id);
        return atualizado;
    }

    public void deletar(long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado: id=" + id));
        repository.delete(id);
        logger.info("Aluno deletado: " + aluno.getNome());
    }

    public Optional<Aluno> buscarPorId(long id) {
        return repository.findById(id);
    }

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }
}
