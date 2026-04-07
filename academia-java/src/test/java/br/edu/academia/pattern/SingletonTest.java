package br.edu.academia.pattern;

import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.infrastructure.database.DatabaseConnection;
import br.edu.academia.testutil.TestStubs.*;
import br.edu.academia.domain.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Singleton em DatabaseConnection e AcademiaFacade.
 */
class SingletonTest {

    @BeforeEach
    @AfterEach
    void resetFacade() throws Exception {
        Field field = AcademiaFacade.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void databaseConnectionRetornaMesmaInstancia() {
        DatabaseConnection a = DatabaseConnection.getInstance();
        DatabaseConnection b = DatabaseConnection.getInstance();

        assertSame(a, b, "getInstance() deve retornar sempre a mesma instancia");
    }

    @Test
    void academiaFacadeRetornaMesmaInstanciaAposInit() throws Exception {
        inicializarFacade();

        AcademiaFacade a = AcademiaFacade.getInstance();
        AcademiaFacade b = AcademiaFacade.getInstance();

        assertSame(a, b);
    }

    @Test
    void academiaFacadeThrowsAntesDaInicializacao() {
        assertThrows(IllegalStateException.class, AcademiaFacade::getInstance,
                "getInstance() deve lancar excecao antes de init()");
    }

    @Test
    void academiaFacadeNaoReinicializaAposSegundoChamadaDeInit() throws Exception {
        inicializarFacade();
        AcademiaFacade primeira = AcademiaFacade.getInstance();

        // Segunda chamada a init() nao deve alterar a instancia
        inicializarFacade();
        AcademiaFacade segunda = AcademiaFacade.getInstance();

        assertSame(primeira, segunda);
    }

    @Test
    void databaseConnectionNaoPermiteClone() throws Exception {
        DatabaseConnection db = DatabaseConnection.getInstance();
        // Verifica que DatabaseConnection.clone() esta sobrescrito para lanclar CloneNotSupportedException
        var method = DatabaseConnection.class.getDeclaredMethod("clone");
        method.setAccessible(true);
        assertThrows(CloneNotSupportedException.class, () -> {
            try {
                method.invoke(db);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    // ── helper ──────────────────────────────────────────────────────────────

    private void inicializarFacade() {
        var hist = new HistoricoOperacoes();
        var log  = new NoOpLogger();
        var hasher = new FakePasswordHasher();
        AcademiaFacade.init(
                new AlunoService(new InMemoryAlunoRepository(), new FixedMatriculaGenerator(1), hasher, hist, log),
                new ProfessorService(new InMemoryProfessorRepository(), hasher, hist, log),
                new AtendenteService(new InMemoryAtendenteRepository(), hasher, hist, log),
                new AdministradorService(new InMemoryAdministradorRepository(), hasher, hist, log),
                new TurmaService(new InMemoryTurmaRepository(), hist, log),
                new TreinoService(new InMemoryTreinoRepository(), hist, log)
        );
    }
}
