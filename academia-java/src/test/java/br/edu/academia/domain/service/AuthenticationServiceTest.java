package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private AuthenticationService service;
    private InMemoryAlunoRepository alunoRepo;
    private InMemoryProfessorRepository professorRepo;
    private InMemoryAtendenteRepository atendenteRepo;
    private InMemoryAdministradorRepository adminRepo;
    private InMemoryLoginHistoryRepository historyRepo;
    private FakePasswordHasher hasher;

    // Credenciais usadas em todos os testes
    private static final String LOGIN_ADM  = "adminlog";
    private static final String LOGIN_PROF = "proflog";
    private static final String LOGIN_ATD  = "atdlog";
    private static final String LOGIN_ALU  = "alulog";
    private static final String SENHA      = "Senha@abc1";

    @BeforeEach
    void setUp() {
        alunoRepo     = new InMemoryAlunoRepository();
        professorRepo = new InMemoryProfessorRepository();
        atendenteRepo = new InMemoryAtendenteRepository();
        adminRepo     = new InMemoryAdministradorRepository();
        historyRepo   = new InMemoryLoginHistoryRepository();
        hasher        = new FakePasswordHasher();

        // Pré-popular cada repo com 1 usuario
        Administrador admin = new Administrador.Builder()
                .nome("Admin").dataNascimento(java.time.LocalDate.of(1980,1,1))
                .email("admin@e.com").login(LOGIN_ADM).senhaHash(hasher.hash(SENHA)).build();
        adminRepo.save(admin);

        Professor prof = new Professor.Builder()
                .nome("Professor").dataNascimento(java.time.LocalDate.of(1985,1,1))
                .email("prof@e.com").login(LOGIN_PROF).senhaHash(hasher.hash(SENHA)).build();
        professorRepo.save(prof);

        Atendente atd = new Atendente.Builder()
                .nome("Atendente").dataNascimento(java.time.LocalDate.of(1990,1,1))
                .email("atd@e.com").login(LOGIN_ATD).senhaHash(hasher.hash(SENHA)).build();
        atendenteRepo.save(atd);

        Aluno aluno = new Aluno.Builder()
                .nome("Aluno").dataNascimento(java.time.LocalDate.of(2000,1,1))
                .email("alu@e.com").matricula(111).login(LOGIN_ALU).senhaHash(hasher.hash(SENHA)).build();
        alunoRepo.save(aluno);

        service = new AuthenticationService(
                alunoRepo, professorRepo, atendenteRepo, adminRepo,
                historyRepo, hasher, new NoOpLogger());
    }

    @Test
    void deveAutenticarAdministrador() {
        Optional<AuthResult> res = service.autenticar(LOGIN_ADM, SENHA);

        assertTrue(res.isPresent());
        assertEquals(TipoEntidade.ADMINISTRADOR, res.get().getRole());
        assertEquals("Admin", res.get().getUsuario().getNome());
    }

    @Test
    void deveAutenticarProfessor() {
        Optional<AuthResult> res = service.autenticar(LOGIN_PROF, SENHA);

        assertTrue(res.isPresent());
        assertEquals(TipoEntidade.PROFESSOR, res.get().getRole());
    }

    @Test
    void deveAutenticarAtendente() {
        Optional<AuthResult> res = service.autenticar(LOGIN_ATD, SENHA);

        assertTrue(res.isPresent());
        assertEquals(TipoEntidade.ATENDENTE, res.get().getRole());
    }

    @Test
    void deveAutenticarAluno() {
        Optional<AuthResult> res = service.autenticar(LOGIN_ALU, SENHA);

        assertTrue(res.isPresent());
        assertEquals(TipoEntidade.ALUNO, res.get().getRole());
    }

    @Test
    void deveRetornarVazioParaLoginInexistente() {
        Optional<AuthResult> res = service.autenticar("naoexiste", SENHA);

        assertTrue(res.isEmpty());
    }

    @Test
    void deveRetornarVazioParaSenhaErrada() {
        Optional<AuthResult> res = service.autenticar(LOGIN_ADM, "senhaerrada");

        assertTrue(res.isEmpty());
    }

    @Test
    void deveRetornarVazioParaLoginVazio() {
        Optional<AuthResult> res = service.autenticar("", SENHA);

        assertTrue(res.isEmpty());
    }

    @Test
    void deveRetornarVazioParaLoginNull() {
        Optional<AuthResult> res = service.autenticar(null, SENHA);

        assertTrue(res.isEmpty());
    }

    @Test
    void deveRegistrarLoginNoHistorico() {
        service.autenticar(LOGIN_ADM, SENHA);

        assertEquals(1, historyRepo.total());
    }

    @Test
    void naoDeveRegistrarLoginFalhoNoHistorico() {
        service.autenticar("naoexiste", SENHA);

        assertEquals(0, historyRepo.total());
    }

    @Test
    void deveRetornarRoleCorretaParaCadaTipo() {
        assertEquals(TipoEntidade.ADMINISTRADOR, service.autenticar(LOGIN_ADM,  SENHA).get().getRole());
        assertEquals(TipoEntidade.PROFESSOR,     service.autenticar(LOGIN_PROF, SENHA).get().getRole());
        assertEquals(TipoEntidade.ATENDENTE,     service.autenticar(LOGIN_ATD,  SENHA).get().getRole());
        assertEquals(TipoEntidade.ALUNO,         service.autenticar(LOGIN_ALU,  SENHA).get().getRole());
    }
}
