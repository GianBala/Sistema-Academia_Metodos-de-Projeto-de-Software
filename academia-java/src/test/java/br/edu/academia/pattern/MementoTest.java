package br.edu.academia.pattern;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Memento:
 * - Desfazer criacao remove entidade
 * - Desfazer atualizacao restaura estado anterior
 * - Ordem LIFO do historico
 */
class MementoTest {

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
    void desfazerCriacaoDeletaEntidade() {
        service.cadastrar("Maria", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        assertEquals(1, repository.findAll().size());

        historico.desfazer(); // desfaz criacao

        assertTrue(repository.findAll().isEmpty(),
                "Desfazer criacao deve remover a entidade do repositorio");
    }

    @Test
    void desfazerAtualizacaoRestauraEstadoAnterior() {
        Aluno original = service.cadastrar("Nome Original", TestStubs.VALID_DATE,
                "orig@email.com", "logoriginal", TestStubs.VALID_PASSWORD);

        service.atualizar(original.getId(), "Nome Alterado", TestStubs.VALID_DATE,
                "novo@email.com", "logalterado", TestStubs.VALID_PASSWORD);

        // Estado atual deve ser "Nome Alterado"
        assertEquals("Nome Alterado", repository.findById(original.getId()).get().getNome());

        historico.desfazer(); // desfaz atualizacao (restaura anterior)

        // Estado restaurado deve ser "Nome Original"
        assertEquals("Nome Original", repository.findById(original.getId()).get().getNome(),
                "Desfazer atualizacao deve restaurar o estado anterior");
    }

    @Test
    void ordemLIFO() {
        Aluno a1 = service.cadastrar("Primeiro", TestStubs.VALID_DATE,
                "a@email.com", "logprimeiro", TestStubs.VALID_PASSWORD);
        service.cadastrar("Segundo", TestStubs.VALID_DATE,
                "b@email.com", "logsegundo", TestStubs.VALID_PASSWORD);

        assertEquals(2, repository.findAll().size());

        historico.desfazer(); // desfaz criacao de "Segundo"
        assertEquals(1, repository.findAll().size());
        assertEquals("Primeiro", repository.findAll().get(0).getNome());

        historico.desfazer(); // desfaz criacao de "Primeiro"
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void temHistoricoRefletePilha() {
        assertFalse(historico.temHistorico());

        service.cadastrar("A", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, "logaaa", TestStubs.VALID_PASSWORD);
        assertTrue(historico.temHistorico());
        assertEquals(1, historico.tamanho());

        historico.desfazer();
        assertFalse(historico.temHistorico());
    }

    @Test
    void desfazerSemHistoricoRetornaVazio() {
        assertTrue(historico.desfazer().isEmpty());
    }

    @Test
    void mementoArmazenaDescricaoCorreta() {
        service.cadastrar("Maria", TestStubs.VALID_DATE,
                TestStubs.VALID_EMAIL, TestStubs.VALID_LOGIN, TestStubs.VALID_PASSWORD);

        var memento = historico.desfazer();

        assertTrue(memento.isPresent());
        assertTrue(memento.get().getDescricao().contains("Maria"));
    }
}
