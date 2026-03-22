package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.repository.AlunoRepository;
import br.edu.academia.domain.validation.DataNascimentoValidator;
import br.edu.academia.domain.validation.EmailValidator;
import br.edu.academia.domain.validation.ValidationResult;

import java.time.LocalDate;
import java.util.List;

public class AlunoService {

    private final AlunoRepository repository;
    private final MatriculaGenerator matriculaGenerator;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;

    public AlunoService(AlunoRepository repository, MatriculaGenerator matriculaGenerator) {
        this.repository = repository;
        this.matriculaGenerator = matriculaGenerator;
        this.dataValidator = new DataNascimentoValidator();
        this.emailValidator = new EmailValidator();
    }

    public Aluno cadastrar(String nome, String dataNascimentoStr, String email) {
        ValidationResult result = ValidationResult.merge(List.of(
                dataValidator.validate(dataNascimentoStr),
                emailValidator.validate(email)
        ));

        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorMessage());
        }

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);
        int matricula = matriculaGenerator.generate();

        Aluno aluno = new Aluno(nome, dataNascimento, email, matricula);
        repository.save(aluno);
        return aluno;
    }

    public List<Aluno> listarTodos() {
        return repository.findAll();
    }

    public Aluno atualizar(long id, String nome, String dataNascimentoStr, String email) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno nao encontrado com id: " + id));

        ValidationResult result = ValidationResult.merge(List.of(
                dataValidator.validate(dataNascimentoStr),
                emailValidator.validate(email)
        ));

        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorMessage());
        }

        aluno.setNome(nome);
        aluno.setDataNascimento(dataValidator.parse(dataNascimentoStr));
        aluno.setEmail(email);
        repository.update(aluno);
        return aluno;
    }
}
