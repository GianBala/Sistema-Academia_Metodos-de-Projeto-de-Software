package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntidadeTest {

    @Test
    void deveCalcularIdadeCorretamente() {
        Aluno aluno = new Aluno.Builder()
                .nome("teste")
                .dataNascimento(LocalDate.now().minusYears(25))
                .email("teste@email.com")
                .matricula(1)
                .build();

        assertEquals(25, aluno.getIdade());
    }

    @Test
    void deveFormatarNomeComTitleCase() {
        Aluno aluno = new Aluno.Builder()
                .nome("joao silva")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .email("joao@email.com")
                .matricula(1)
                .build();

        assertEquals("Joao Silva", aluno.getNome());
    }

    @Test
    void deveRetornarInfosFormatadas() {
        Aluno aluno = new Aluno.Builder()
                .nome("Maria")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .email("maria@email.com")
                .matricula(123)
                .build();

        String infos = aluno.infos();

        assertTrue(infos.contains("Maria"));
        assertTrue(infos.contains("maria@email.com"));
        assertTrue(infos.contains("Aluno"));
        assertTrue(infos.contains("123"));
    }

    @Test
    void deveDefinirIdCorretamente() {
        Aluno aluno = new Aluno.Builder()
                .nome("teste")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .email("t@e.com")
                .matricula(1)
                .build();
        aluno.setId(42);

        assertEquals(42, aluno.getId());
    }
}