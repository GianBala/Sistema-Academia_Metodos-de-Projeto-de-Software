package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.memento.CadastroMemento;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.ProfessorRepository;
import br.edu.academia.domain.validation.DataNascimentoValidator;
import br.edu.academia.domain.validation.EmailValidator;
import br.edu.academia.domain.validation.IdadeMinimaPredicate;
import br.edu.academia.domain.validation.ValidationResult;

import java.time.LocalDate;
import java.util.List;

public class ProfessorService {

    private static final int IDADE_MINIMA_FUNCIONARIO = 18;

    private final ProfessorRepository repository;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final IdadeMinimaPredicate idadeValidator;
    private final HistoricoOperacoes historico;

    public ProfessorService(ProfessorRepository repository, HistoricoOperacoes historico) {
        this.repository = repository;
        this.historico = historico;
        this.dataValidator = new DataNascimentoValidator();
        this.emailValidator = new EmailValidator();
        this.idadeValidator = new IdadeMinimaPredicate(IDADE_MINIMA_FUNCIONARIO);
    }

    public Professor cadastrar(String nome, String dataNascimentoStr, String email) {
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

        
        Professor professor = new Professor.Builder()
                    .nome(nome)
                    .dataNascimento(dataNascimento)
                    .email(email)
                    .build();
        repository.save(professor);
        historico.registrar(new CadastroMemento(professor.getId(), TipoEntidade.PROFESSOR, "Professor: " + professor.getNome()));
        return professor;
    }

    public List<Professor> listarTodos() {
        return repository.findAll();
    }
}
