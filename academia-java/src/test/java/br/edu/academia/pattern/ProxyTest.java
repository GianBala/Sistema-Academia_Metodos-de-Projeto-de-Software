package br.edu.academia.pattern;

import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.service.SessionContext;
import br.edu.academia.infrastructure.proxy.*;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Proxy (controle de acesso por role).
 */
class ProxyTest {

    private InMemoryAlunoRepository alunoDelegate;
    private InMemoryAdministradorRepository adminDelegate;
    private InMemoryProfessorRepository professorDelegate;
    private InMemoryAtendenteRepository atendenteDelegate;

    private AlunoRepositoryProxy alunoProxy;
    private AdministradorRepositoryProxy adminProxy;
    private ProfessorRepositoryProxy professorProxy;
    private AtendenteRepositoryProxy atendenteProxy;

    @BeforeEach
    void setUp() {
        alunoDelegate    = new InMemoryAlunoRepository();
        adminDelegate    = new InMemoryAdministradorRepository();
        professorDelegate = new InMemoryProfessorRepository();
        atendenteDelegate = new InMemoryAtendenteRepository();

        alunoProxy    = new AlunoRepositoryProxy(alunoDelegate);
        adminProxy    = new AdministradorRepositoryProxy(adminDelegate);
        professorProxy = new ProfessorRepositoryProxy(professorDelegate);
        atendenteProxy = new AtendenteRepositoryProxy(atendenteDelegate);
    }

    @AfterEach
    void tearDown() {
        SessionContext.clearSession();
    }

    // ── Administrador — pode tudo ────────────────────────────────────────────

    @Test
    void adminPodeSalvarEmAlunoRepo() {
        setRole(TipoEntidade.ADMINISTRADOR);
        assertDoesNotThrow(() -> alunoProxy.save(TestStubs.criarAluno("Aluno", 1)));
    }

    @Test
    void adminPodeSalvarEmAdminRepo() {
        setRole(TipoEntidade.ADMINISTRADOR);
        assertDoesNotThrow(() -> adminProxy.save(TestStubs.criarAdministrador("Admin")));
    }

    @Test
    void adminPodeDeletarEmQualquerRepo() {
        setRole(TipoEntidade.ADMINISTRADOR);
        alunoProxy.save(TestStubs.criarAluno("X", 1));
        long id = alunoDelegate.findAll().get(0).getId();
        assertDoesNotThrow(() -> alunoProxy.delete(id));
    }

    // ── Atendente — ALUNO e PROFESSOR ───────────────────────────────────────

    @Test
    void atendentePodeSalvarEmAlunoRepo() {
        setRole(TipoEntidade.ATENDENTE);
        assertDoesNotThrow(() -> alunoProxy.save(TestStubs.criarAluno("Aluno", 1)));
    }

    @Test
    void atendentePodeSalvarEmProfessorRepo() {
        setRole(TipoEntidade.ATENDENTE);
        assertDoesNotThrow(() -> professorProxy.save(TestStubs.criarProfessor("Prof")));
    }

    @Test
    void atendenteNaoPodeSalvarEmAdminRepo() {
        setRole(TipoEntidade.ATENDENTE);
        assertThrows(SecurityException.class,
                () -> adminProxy.save(TestStubs.criarAdministrador("Admin")));
    }

    @Test
    void atendenteNaoPodeSalvarEmAtendenteRepo() {
        setRole(TipoEntidade.ATENDENTE);
        assertThrows(SecurityException.class,
                () -> atendenteProxy.save(TestStubs.criarAtendente("Atd")));
    }

    // ── Aluno — somente leitura ──────────────────────────────────────────────

    @Test
    void alunoNaoPodeSalvarEmNenhumRepo() {
        setRole(TipoEntidade.ALUNO);
        assertThrows(SecurityException.class,
                () -> alunoProxy.save(TestStubs.criarAluno("X", 1)));
    }

    @Test
    void alunoNaoPodeDeletarEmNenhumRepo() {
        setRole(TipoEntidade.ALUNO);
        assertThrows(SecurityException.class, () -> alunoProxy.delete(1L));
    }

    @Test
    void alunoNaoPodeAtualizarEmNenhumRepo() {
        setRole(TipoEntidade.ALUNO);
        assertThrows(SecurityException.class,
                () -> alunoProxy.update(TestStubs.criarAluno("X", 1)));
    }

    // ── Professor — somente leitura ──────────────────────────────────────────

    @Test
    void professorNaoPodeSalvarEmNenhumRepo() {
        setRole(TipoEntidade.PROFESSOR);
        assertThrows(SecurityException.class,
                () -> alunoProxy.save(TestStubs.criarAluno("X", 1)));
    }

    // ── Leitura permitida para todos ─────────────────────────────────────────

    @Test
    void leituraPermitidaParaAdmin() {
        setRole(TipoEntidade.ADMINISTRADOR);
        assertDoesNotThrow(() -> alunoProxy.findAll());
        assertDoesNotThrow(() -> adminProxy.findAll());
    }

    @Test
    void leituraPermitidaParaAtendente() {
        setRole(TipoEntidade.ATENDENTE);
        assertDoesNotThrow(() -> alunoProxy.findAll());
        assertDoesNotThrow(() -> adminProxy.findAll());
    }

    @Test
    void leituraPermitidaParaAluno() {
        setRole(TipoEntidade.ALUNO);
        assertDoesNotThrow(() -> alunoProxy.findAll());
    }

    @Test
    void leituraPermitidaParaProfessor() {
        setRole(TipoEntidade.PROFESSOR);
        assertDoesNotThrow(() -> alunoProxy.findAll());
    }

    // ── Role null (pre-login) — permite seed de admin ────────────────────────

    @Test
    void roleNullPermiteSalvarParaSeed() {
        // SessionContext.getCurrentRole() == null (sem sessao ativa)
        SessionContext.clearSession();
        assertDoesNotThrow(() -> adminProxy.save(TestStubs.criarAdministrador("Admin Padrao")));
    }

    // ── helper ───────────────────────────────────────────────────────────────

    private void setRole(TipoEntidade role) {
        SessionContext.setSession(TestStubs.criarAdministrador("User"), role);
    }
}
