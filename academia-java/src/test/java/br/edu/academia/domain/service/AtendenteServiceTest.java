package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class AtendenteServiceTest {

    private AtendenteService service;
    private InMemoryAtendenteRepository repository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        repository = new InMemoryAtendenteRepository();
        service    = new AtendenteService(repository,
                new FakePasswordHasher(),
                new HistoricoOperacoes(),
                new NoOpLogger());
    }

    @Test
    void deveCadastrarAtendenteMaiorDe18() {
        Atendente atd = service.cadastrar("Ana", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertEquals("Ana", atd.getNome());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveRejeitarAtendenteMenorDe18() {
        String data = LocalDate.now().minusYears(17).format(FMT);

        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Jovem", data,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Ana", TestStubs.VALID_DATE,
                        "invalido", TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveRejeitarLoginComNumeros() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Ana", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, "log123", TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveRejeitarSenhaFraca() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Ana", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, "fraca"));
    }

    @Test
    void deveListarTodos() {
        service.cadastrar("Ana", TestStubs.VALID_DATE, "ana@e.com", "logana", TestStubs.VALID_PASSWORD);
        service.cadastrar("Bia", TestStubs.VALID_DATE, "bia@e.com", "logbia", TestStubs.VALID_PASSWORD);

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void deveBuscarPorId() {
        Atendente atd = service.cadastrar("Carlos", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertTrue(service.buscarPorId(atd.getId()).isPresent());
        assertTrue(service.buscarPorId(999L).isEmpty());
    }

    @Test
    void deveAtualizarAtendente() {
        Atendente atd = service.cadastrar("Original", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        Atendente atualizado = service.atualizar(atd.getId(), "Novo Nome",
                TestStubs.VALID_DATE, "novo@email.com", "novologin", TestStubs.VALID_PASSWORD);

        assertEquals("Novo Nome", atualizado.getNome());
    }

    @Test
    void deveDeletarAtendente() {
        Atendente atd = service.cadastrar("Para Deletar", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        service.deletar(atd.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(999L, "Nome", TestStubs.VALID_DATE,
                        TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD));
    }

    @Test
    void deveLancarExcecaoAoDeletarInexistente() {
        assertThrows(IllegalArgumentException.class, () -> service.deletar(999L));
    }
}
