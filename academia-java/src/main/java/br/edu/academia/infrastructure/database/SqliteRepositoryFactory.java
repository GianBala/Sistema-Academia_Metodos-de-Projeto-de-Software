package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.repository.*;

import java.sql.Connection;

public class SqliteRepositoryFactory implements RepositoryFactory {

    private final Connection connection;

    public SqliteRepositoryFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AlunoRepository createAlunoRepository() {
        return new SqliteAlunoRepository(connection);
    }

    @Override
    public ProfessorRepository createProfessorRepository() {
        return new SqliteProfessorRepository(connection);
    }

    @Override
    public AtendenteRepository createAtendenteRepository() {
        return new SqliteAtendenteRepository(connection);
    }

    @Override
    public AdministradorRepository createAdministradorRepository() {
        return new SqliteAdministradorRepository(connection);
    }

    @Override
    public RegistroAcessoRepository createRegistroAcessoRepository() {
        return new SqliteRegistroAcessoRepository(connection);
    }
}
