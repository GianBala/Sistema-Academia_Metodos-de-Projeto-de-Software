package br.edu.academia.pattern;

import br.edu.academia.domain.facade.command.FacadeCommand;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.service.TreinoService;
import br.edu.academia.testutil.TestStubs.InMemoryTreinoRepository;
import br.edu.academia.testutil.TestStubs.NoOpLogger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Command:
 * 1. FacadeCommand — command com retorno usado no Facade
 * 2. Command (UI) — command sem retorno, testado via HistoricoOperacoes
 */
class CommandPatternTest {

    @Test
    void facadeCommandRetornaResultado() {
        FacadeCommand<Integer> cmd = () -> 42;

        assertEquals(42, cmd.execute());
    }

    @Test
    void facadeCommandEncapsulaLogicaComplexaERetornaResultado() {
        InMemoryTreinoRepository repo = new InMemoryTreinoRepository();
        TreinoService service = new TreinoService(repo, new HistoricoOperacoes(), new NoOpLogger());

        // FacadeCommand encapsulando cadastro de treino
        FacadeCommand<br.edu.academia.domain.entity.Treino> cmd =
                () -> service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);

        var treino = cmd.execute();

        assertNotNull(treino);
        assertEquals("Supino", treino.getExercicio());
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void facadeCommandPodeRetornarVoid() {
        AtomicBoolean executado = new AtomicBoolean(false);
        FacadeCommand<Void> cmd = () -> { executado.set(true); return null; };

        cmd.execute();

        assertTrue(executado.get());
    }

    @Test
    void historicoOperacoesExecutaCommandsRegistrados() {
        HistoricoOperacoes historico = new HistoricoOperacoes();
        AtomicBoolean undoExecutado = new AtomicBoolean(false);

        // Registrar um memento (que e internamente um Command via Runnable)
        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO, "Operacao", () -> undoExecutado.set(true)));

        historico.desfazer();

        assertTrue(undoExecutado.get(),
                "O command de undo deve ser executado ao chamar desfazer()");
    }

    @Test
    void multiplosCommandsExecutadosNaOrdemCorreta() {
        HistoricoOperacoes historico = new HistoricoOperacoes();
        StringBuilder ordem = new StringBuilder();

        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "1", () -> ordem.append("A")));
        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "2", () -> ordem.append("B")));
        historico.registrar(new OperacaoMemento(TipoOperacao.CRIACAO, "3", () -> ordem.append("C")));

        historico.desfazer(); // executa C (LIFO)
        historico.desfazer(); // executa B
        historico.desfazer(); // executa A

        assertEquals("CBA", ordem.toString());
    }
}
