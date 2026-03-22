package br.edu.academia.domain.repository;

public interface RepositoryFactory {

    AlunoRepository createAlunoRepository();

    ProfessorRepository createProfessorRepository();

    AtendenteRepository createAtendenteRepository();

    AdministradorRepository createAdministradorRepository();

    RegistroAcessoRepository createRegistroAcessoRepository();
}
