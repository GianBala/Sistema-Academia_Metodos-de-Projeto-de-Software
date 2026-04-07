package br.edu.academia.pattern;

import br.edu.academia.domain.repository.*;
import br.edu.academia.infrastructure.database.DatabaseConnection;
import br.edu.academia.infrastructure.database.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Factory Method (RepositoryFactory + SqliteRepositoryFactory).
 */
class FactoryMethodTest {

    private DatabaseConnection db;
    private SqliteRepositoryFactory factory;

    @BeforeEach
    void setUp() {
        db = new DatabaseConnection("jdbc:sqlite::memory:");
        factory = new SqliteRepositoryFactory(db.getConnection());
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void createAlunoRepositoryRetornaInstanciaCorreta() {
        AlunoRepository repo = factory.createAlunoRepository();
        assertInstanceOf(SqliteAlunoRepository.class, repo);
    }

    @Test
    void createProfessorRepositoryRetornaInstanciaCorreta() {
        ProfessorRepository repo = factory.createProfessorRepository();
        assertInstanceOf(SqliteProfessorRepository.class, repo);
    }

    @Test
    void createAtendenteRepositoryRetornaInstanciaCorreta() {
        AtendenteRepository repo = factory.createAtendenteRepository();
        assertInstanceOf(SqliteAtendenteRepository.class, repo);
    }

    @Test
    void createAdministradorRepositoryRetornaInstanciaCorreta() {
        AdministradorRepository repo = factory.createAdministradorRepository();
        assertInstanceOf(SqliteAdministradorRepository.class, repo);
    }

    @Test
    void createTurmaRepositoryRetornaInstanciaCorreta() {
        TurmaRepository repo = factory.createTurmaRepository();
        assertInstanceOf(SqliteTurmaRepository.class, repo);
    }

    @Test
    void createTreinoRepositoryRetornaInstanciaCorreta() {
        TreinoRepository repo = factory.createTreinoRepository();
        assertInstanceOf(SqliteTreinoRepository.class, repo);
    }

    @Test
    void createLoginHistoryRepositoryRetornaInstanciaCorreta() {
        LoginHistoryRepository repo = factory.createLoginHistoryRepository();
        assertInstanceOf(SqliteLoginHistoryRepository.class, repo);
    }

    @Test
    void factoryRetornaInstanciasDiferentesEmCadaChamada() {
        AlunoRepository r1 = factory.createAlunoRepository();
        AlunoRepository r2 = factory.createAlunoRepository();
        assertNotSame(r1, r2, "Cada chamada ao factory deve criar uma nova instancia");
    }
}
