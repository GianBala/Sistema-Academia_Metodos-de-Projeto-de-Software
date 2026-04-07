package br.edu.academia.domain.repository;

/**
 * Factory Method abstrato — define a interface para criação de repositórios.
 * Subclasses concretas (ex: SqliteRepositoryFactory) implementam os métodos
 * para cada tecnologia de persistência.
 */
public abstract class RepositoryFactory {

    public abstract AlunoRepository createAlunoRepository();
    public abstract ProfessorRepository createProfessorRepository();
    public abstract AtendenteRepository createAtendenteRepository();
    public abstract AdministradorRepository createAdministradorRepository();
    public abstract TurmaRepository createTurmaRepository();
    public abstract TreinoRepository createTreinoRepository();
    public abstract LoginHistoryRepository createLoginHistoryRepository();
}
