package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorServiceTest {

    private ProfessorService service;
    private InMemoryProfessorRepository repository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        repository = new InMemoryProfessorRepository();
        service    = new ProfessorService(repository,
                new FakePasswordHasher(),
                new HistoricoOperacoes(),
                new NoOpLogger());
    }

    @Test
    void deveCadastrarProfessorMaiorDe18() {
        String data = LocalDate.now().minusYears(30).format(FMT);

        Professor prof = service.cadastrar("Carlos", data,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertEquals("Carlos", prof.getNome());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveRejeitarProfessorMenorDe18() {
        String data = LocalDate.now().minusYears(17).format(FMT);

        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Jovem", data,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Carlos", TestStubs.VALID_DATE,
                        "invalido", TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveRejeitarLoginComNumeros() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Carlos", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, "log123", TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveRejeitarSenhaFraca() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Carlos", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, "fraca"));
    }

    @Test
    void deveListarTodos() {
        service.cadastrar("Prof A", TestStubs.VALID_DATE, "a@email.com", "profaaa", TestStubs.VALID_PASSWORD);
        service.cadastrar("Prof B", TestStubs.VALID_DATE, "b@email.com", "profbbb", TestStubs.VALID_PASSWORD);

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void deveAtualizarProfessor() {
        Professor prof = service.cadastrar("Original", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        Professor atualizado = service.atualizar(prof.getId(), "Novo Nome",
                TestStubs.VALID_DATE, "novo@email.com", "novologin", TestStubs.VALID_PASSWORD);

        assertEquals("Novo Nome", atualizado.getNome());
    }

    @Test
    void deveDeletarProfessor() {
        Professor prof = service.cadastrar("Para Deletar", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        service.deletar(prof.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(999L, "Nome", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }
}
