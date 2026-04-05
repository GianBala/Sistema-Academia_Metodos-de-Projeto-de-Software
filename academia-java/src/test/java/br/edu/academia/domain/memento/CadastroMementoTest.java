package br.edu.academia.domain.memento;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CadastroMementoTest {

    @Test
    void deveArmazenarCamposCorretamente() {
        CadastroMemento memento = new CadastroMemento(42L, TipoEntidade.ALUNO, "Aluno: Maria");

        assertEquals(42L, memento.getEntidadeId());
        assertEquals(TipoEntidade.ALUNO, memento.getTipoEntidade());
        assertEquals("Aluno: Maria", memento.getDescricao());
        assertNotNull(memento.getTimestamp());
    }

    @Test
    void deveTerTimestampPreenchidoNaCriacao() {
        var antes = java.time.LocalDateTime.now();
        CadastroMemento memento = new CadastroMemento(1L, TipoEntidade.PROFESSOR, "Professor: Carlos");
        var depois = java.time.LocalDateTime.now();

        assertFalse(memento.getTimestamp().isBefore(antes));
        assertFalse(memento.getTimestamp().isAfter(depois));
    }
}
