package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.repository.AtendenteRepository;
import br.edu.academia.domain.validation.DataNascimentoValidator;
import br.edu.academia.domain.validation.EmailValidator;
import br.edu.academia.domain.validation.IdadeMinimaPredicate;
import br.edu.academia.domain.validation.ValidationResult;

import java.time.LocalDate;
import java.util.List;

public class AtendenteService {

    private static final int IDADE_MINIMA_FUNCIONARIO = 18;

    private final AtendenteRepository repository;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final IdadeMinimaPredicate idadeValidator;

    public AtendenteService(AtendenteRepository repository) {
        this.repository = repository;
        this.dataValidator = new DataNascimentoValidator();
        this.emailValidator = new EmailValidator();
        this.idadeValidator = new IdadeMinimaPredicate(IDADE_MINIMA_FUNCIONARIO);
    }

    public Atendente cadastrar(String nome, String dataNascimentoStr, String email) {
        ValidationResult dateResult = dataValidator.validate(dataNascimentoStr);
        if (!dateResult.isValid()) {
            throw new IllegalArgumentException(dateResult.getErrorMessage());
        }

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);

        ValidationResult result = ValidationResult.merge(List.of(
                emailValidator.validate(email),
                idadeValidator.validate(dataNascimento)
        ));

        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorMessage());
        }

        Atendente atendente = new Atendente.Builder()
                    .nome(nome)
                    .dataNascimento(dataNascimento)
                    .email(email)
                    .build();
        repository.save(atendente);
        return atendente;
    }

    public List<Atendente> listarTodos() {
        return repository.findAll();
    }
}
