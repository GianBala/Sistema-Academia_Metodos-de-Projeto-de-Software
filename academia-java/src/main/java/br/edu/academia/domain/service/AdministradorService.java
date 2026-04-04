package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.repository.AdministradorRepository;
import br.edu.academia.domain.validation.*;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDate;
import java.util.List;

public class AdministradorService {

    private final AdministradorRepository repository;
    private final PasswordHasher passwordHasher;
    private final DataNascimentoValidator dataValidator;
    private final EmailValidator emailValidator;
    private final LoginValidator loginValidator;

    public AdministradorService(AdministradorRepository repository, PasswordHasher passwordHasher) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.dataValidator = new DataNascimentoValidator();
        this.emailValidator = new EmailValidator();
        this.loginValidator = new LoginValidator();
    }

    public Administrador cadastrar(String nome, String dataNascimentoStr, String email,
                                   String login, String senha) {
        ValidationResult dateResult = dataValidator.validate(dataNascimentoStr);
        if (!dateResult.isValid()) {
            throw new IllegalArgumentException(dateResult.getErrorMessage());
        }

        ValidationResult result = ValidationResult.merge(List.of(
                emailValidator.validate(email),
                loginValidator.validate(login),
                new SenhaValidator(nome, email).validate(senha)
        ));

        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrorMessage());
        }

        LocalDate dataNascimento = dataValidator.parse(dataNascimentoStr);
        String senhaHash = passwordHasher.hash(senha);

        Administrador admin = new Administrador.Builder()
                    .nome(nome)
                    .dataNascimento(dataNascimento)
                    .email(email)
                    .login(login)
                    .senhaHash(senhaHash)
                    .build();
        repository.save(admin);
        return admin;
    }

    public List<Administrador> listarTodos() {
        return repository.findAll();
    }
}
