package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntidadeTest {

    @Test
    void deveCalcularIdadeCorretamente() {
        LocalDate nascimento = LocalDate.now().minusYears(25);
        Aluno aluno = new Aluno("teste", nascimento, "teste@email.com", 1);

        assertEquals(25, aluno.getIdade());
    }

    @Test
    void deveFormatarNomeComTitleCase() {
        Aluno aluno = new Aluno("joao silva", LocalDate.of(2000, 1, 1), "joao@email.com", 1);

        assertEquals("Joao Silva", aluno.getNome());
    }

    @Test
    void deveRetornarInfosFormatadas() {
        LocalDate nascimento = LocalDate.of(2000, 1, 1);
        Aluno aluno = new Aluno("Maria", nascimento, "maria@email.com", 123);

        String infos = aluno.infos();

        assertTrue(infos.contains("Maria"));
        assertTrue(infos.contains("maria@email.com"));
        assertTrue(infos.contains("Aluno"));
        assertTrue(infos.contains("123"));
    }

    @Test
    void deveDefinirIdCorretamente() {
        Aluno aluno = new Aluno("teste", LocalDate.of(2000, 1, 1), "t@e.com", 1);
        aluno.setId(42);

        assertEquals(42, aluno.getId());
    }
}
