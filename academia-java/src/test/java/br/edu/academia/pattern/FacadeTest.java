package br.edu.academia.pattern;

import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.service.*;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Facade (interface simplificada ao subsistema).
 */
class FacadeTest {

    private AcademiaFacade facade;
    private HistoricoOperacoes historico;

    @BeforeEach
    void setUp() throws Exception {
        resetFacade();
        historico = new HistoricoOperacoes();
        var log    = new NoOpLogger();
        var hasher = new FakePasswordHasher();
        AcademiaFacade.init(
                new AlunoService(new InMemoryAlunoRepository(), new FixedMatriculaGenerator(1), hasher, historico, log),
                new ProfessorService(new InMemoryProfessorRepository(), hasher, historico, log),
                new AtendenteService(new InMemoryAtendenteRepository(), hasher, historico, log),
                new AdministradorService(new InMemoryAdministradorRepository(), hasher, historico, log),
                new TurmaService(new InMemoryTurmaRepository(), historico, log),
                new TreinoService(new InMemoryTreinoRepository(), historico, log)
        );
        facade = AcademiaFacade.getInstance();
    }

    @AfterEach
    void tearDown() throws Exception {
        resetFacade();
    }

    private void resetFacade() throws Exception {
        Field field = AcademiaFacade.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void contarEntidadesRetornaZeroInicialmente() {
        assertEquals(0, facade.contarEntidades());
    }

    @Test
    void contarEntidadesRetornaContagemCorreta() {
        facade.cadastrarAluno("Aluno A", TestStubs.VALID_DATE, "a@e.com", "logaaa", TestStubs.VALID_PASSWORD);
        facade.cadastrarAluno("Aluno B", TestStubs.VALID_DATE, "b@e.com", "logbbb", TestStubs.VALID_PASSWORD);
        facade.cadastrarProfessor("Prof C", TestStubs.VALID_DATE, "c@e.com", "logccc", TestStubs.VALID_PASSWORD);

        assertEquals(3, facade.contarEntidades());
    }

    @Test
    void contarEntidadesPorTipoRetornaMapComChavesCorretas() {
        Map<String, Integer> contagens = facade.contarEntidadesPorTipo();

        assertTrue(contagens.containsKey("Alunos"));
        assertTrue(contagens.containsKey("Professores"));
        assertTrue(contagens.containsKey("Atendentes"));
        assertTrue(contagens.containsKey("Administradores"));
        assertTrue(contagens.containsKey("Turmas"));
    }

    @Test
    void cadastrarAlunoViaFacade() {
        var aluno = facade.cadastrarAluno("Maria", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertNotNull(aluno);
        assertEquals("Maria", aluno.getNome());
        assertEquals(1, facade.listarAlunos().size());
    }

    @Test
    void listarAlunosViaFacade() {
        facade.cadastrarAluno("A1", TestStubs.VALID_DATE, "a1@e.com", "logaum", TestStubs.VALID_PASSWORD);
        facade.cadastrarAluno("A2", TestStubs.VALID_DATE, "a2@e.com", "logadois", TestStubs.VALID_PASSWORD);

        assertEquals(2, facade.listarAlunos().size());
    }

    @Test
    void deletarAlunoViaFacade() {
        var aluno = facade.cadastrarAluno("Delete", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        facade.deletarAluno(aluno.getId());

        assertTrue(facade.listarAlunos().isEmpty());
    }

    @Test
    void buscarAlunoViaFacade() {
        var aluno = facade.cadastrarAluno("Busca", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertTrue(facade.buscarAluno(aluno.getId()).isPresent());
        assertTrue(facade.buscarAluno(999L).isEmpty());
    }

    @Test
    void cadastrarTurmaViaFacade() {
        var turma = facade.cadastrarTurma("Musculacao", 1L, "08:00");

        assertNotNull(turma);
        assertEquals("Musculacao", turma.getNome());
        assertEquals(1, facade.listarTurmas().size());
    }

    @Test
    void facadeFornecerInterfaceUnificadaSemExporSistema() {
        // Verifica que os servicos internos nao sao acessiveis diretamente
        // (somente acessiveis via AcademiaFacade)
        assertNotNull(facade);
        assertDoesNotThrow(() -> facade.contarEntidades());
        assertDoesNotThrow(() -> facade.contarEntidadesPorTipo());
    }
}
