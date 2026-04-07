package br.edu.academia.domain.memento;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class OperacaoMementoTest {

    @Test
    void deveArmazenarCamposCorretamente() {
        OperacaoMemento memento = new OperacaoMemento(
                TipoOperacao.CRIACAO, "Cadastro de aluno: Maria", () -> {});

        assertEquals(TipoOperacao.CRIACAO, memento.getTipoOperacao());
        assertEquals("Cadastro de aluno: Maria", memento.getDescricao());
        assertNotNull(memento.getTimestamp());
    }

    @Test
    void deveTerTimestampPreenchidoNaCriacao() {
        LocalDateTime antes = LocalDateTime.now();
        OperacaoMemento memento = new OperacaoMemento(
                TipoOperacao.ATUALIZACAO, "Atualizacao", () -> {});
        LocalDateTime depois = LocalDateTime.now();

        assertFalse(memento.getTimestamp().isBefore(antes));
        assertFalse(memento.getTimestamp().isAfter(depois));
    }

    @Test
    void desfazerDeveExecutarAcao() {
        AtomicBoolean executado = new AtomicBoolean(false);
        OperacaoMemento memento = new OperacaoMemento(
                TipoOperacao.CRIACAO, "desc", () -> executado.set(true));

        memento.desfazer();

        assertTrue(executado.get());
    }

    @Test
    void deveSuportarTipoAtualizacao() {
        OperacaoMemento memento = new OperacaoMemento(
                TipoOperacao.ATUALIZACAO, "Update", () -> {});

        assertEquals(TipoOperacao.ATUALIZACAO, memento.getTipoOperacao());
    }
}
