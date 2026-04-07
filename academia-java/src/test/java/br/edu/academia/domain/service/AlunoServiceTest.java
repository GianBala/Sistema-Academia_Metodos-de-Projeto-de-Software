package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlunoServiceTest {

    private AlunoService service;
    private InMemoryAlunoRepository repository;
    private HistoricoOperacoes historico;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAlunoRepository();
        historico  = new HistoricoOperacoes();
        service    = new AlunoService(repository,
                new FixedMatriculaGenerator(1234),
                new FakePasswordHasher(),
                historico,
                new NoOpLogger());
    }

    @Test
    void deveCadastrarAlunoComDadosValidos() {
        Aluno aluno = service.cadastrar("Maria Silva", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals(1234, aluno.getMatricula());
        assertTrue(aluno.getSenhaHash().startsWith("hashed_"));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveLancarExcecaoParaEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Maria", TestStubs.VALID_DATE,
                        "emailinvalido", TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveLancarExcecaoParaDataInvalida() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Maria", "data-invalida",
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveLancarExcecaoParaLoginComNumeros() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Maria", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, "login123", TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveLancarExcecaoParaSenhaFraca() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Maria", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, "fraca"));
    }

    @Test
    void deveListarTodos() {
        service.cadastrar("Aluno A", TestStubs.VALID_DATE, "a@email.com", "loginaaa", TestStubs.VALID_PASSWORD);
        service.cadastrar("Aluno B", TestStubs.VALID_DATE, "b@email.com", "loginbbb", TestStubs.VALID_PASSWORD);

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void deveBuscarPorId() {
        Aluno aluno = service.cadastrar("Carlos", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertTrue(service.buscarPorId(aluno.getId()).isPresent());
        assertTrue(service.buscarPorId(999L).isEmpty());
    }

    @Test
    void deveAtualizarAluno() {
        Aluno original = service.cadastrar("Nome Original", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        Aluno atualizado = service.atualizar(original.getId(), "Nome Novo",
                TestStubs.VALID_DATE, "novo@email.com", "novologin", TestStubs.VALID_PASSWORD);

        assertEquals("Nome Novo", atualizado.getNome());
        assertEquals("novo@email.com", atualizado.getEmail());
        assertEquals(original.getMatricula(), atualizado.getMatricula());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(999L, "Nome", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveDeletarAluno() {
        Aluno aluno = service.cadastrar("Para Deletar", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        service.deletar(aluno.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deveLancarExcecaoAoDeletarInexistente() {
        assertThrows(IllegalArgumentException.class, () -> service.deletar(999L));
    }

    @Test
    void deveRegistrarMementoAoCadastrar() {
        service.cadastrar("Maria", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertEquals(1, historico.tamanho());
    }

    @Test
    void deveRegistrarMementoAoAtualizar() {
        Aluno aluno = service.cadastrar("Original", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);
        service.atualizar(aluno.getId(), "Novo Nome", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        // 1 memento de criacao + 1 de atualizacao
        assertEquals(2, historico.tamanho());
    }
}
