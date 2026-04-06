package br.edu.academia.domain.repository;

public abstract class RepositoryFactory {

    public abstract AlunoRepository createAlunoRepository();

    public abstract ProfessorRepository createProfessorRepository();

    public abstract AtendenteRepository createAtendenteRepository();

    public abstract AdministradorRepository createAdministradorRepository();
}
