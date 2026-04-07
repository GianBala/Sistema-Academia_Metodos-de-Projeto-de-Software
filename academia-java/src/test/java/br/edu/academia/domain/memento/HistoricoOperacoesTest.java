package br.edu.academia.domain.memento;

import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.InMemoryAlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoOperacoesTest {

    private HistoricoOperacoes historico;
    private InMemoryAlunoRepository repository;

    @BeforeEach
    void setUp() {
        historico = new HistoricoOperacoes();
        repository = new InMemoryAlunoRepository();
    }

    @Test
    void deveRetornarFalseQuandoHistoricoVazio() {
        assertFalse(historico.temHistorico());
    }

    @Test
    void deveRetornarTrueAposRegistrar() {
        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "desc", () -> {}));
        assertTrue(historico.temHistorico());
    }

    @Test
    void deveRetornarOptionalVazioAoDesfazerSemHistorico() {
        Optional<OperacaoMemento> resultado = historico.desfazer();
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveDesfazerExecutandoAcao() {
        var aluno = TestStubs.criarAluno("Maria", 1);
        repository.save(aluno);
        assertEquals(1, repository.findAll().size());

        long id = aluno.getId();
        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO, "Cadastro: Maria", () -> repository.delete(id)));

        Optional<OperacaoMemento> resultado = historico.desfazer();

        assertTrue(resultado.isPresent());
        assertEquals("Cadastro: Maria", resultado.get().getDescricao());
        assertEquals(0, repository.findAll().size());
        assertFalse(historico.temHistorico());
    }

    @Test
    void deveDesfazerEmOrdemLIFO() {
        AtomicInteger contador = new AtomicInteger(0);

        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "Primeiro", () -> contador.set(1)));
        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "Segundo",  () -> contador.set(2)));

        // Primeiro desfazer deve executar "Segundo" (topo do stack)
        Optional<OperacaoMemento> primeiro = historico.desfazer();
        assertTrue(primeiro.isPresent());
        assertEquals("Segundo", primeiro.get().getDescricao());
        assertEquals(2, contador.get());

        // Segundo desfazer deve executar "Primeiro"
        Optional<OperacaoMemento> segundo = historico.desfazer();
        assertTrue(segundo.isPresent());
        assertEquals("Primeiro", segundo.get().getDescricao());
        assertEquals(1, contador.get());

        assertFalse(historico.temHistorico());
    }

    @Test
    void tamanhoDeveRefletirRegistros() {
        assertEquals(0, historico.tamanho());
        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "A", () -> {}));
        assertEquals(1, historico.tamanho());
        historico.registrar(new OperacaoMemento(TipoOperacao.ATUALIZACAO, "B", () -> {}));
        assertEquals(2, historico.tamanho());
        historico.desfazer();
        assertEquals(1, historico.tamanho());
    }
}
