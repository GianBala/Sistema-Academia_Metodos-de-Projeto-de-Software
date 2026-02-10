package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComMatricula() {
        Aluno aluno = new Aluno("Carlos", LocalDate.of(2000, 5, 15), "carlos@email.com", 1234);

        assertEquals("Carlos", aluno.getNome());
        assertEquals(1234, aluno.getMatricula());
        assertEquals("carlos@email.com", aluno.getEmail());
    }

    @Test
    void infosDeveConterMatricula() {
        Aluno aluno = new Aluno("Ana", LocalDate.of(1999, 3, 20), "ana@email.com", 5678);

        assertTrue(aluno.infos().contains("5678"));
        assertTrue(aluno.infos().contains("Matricula"));
    }
}
