package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComMatricula() {
        Aluno aluno = new Aluno.Builder()
                .nome("Carlos")
                .dataNascimento(LocalDate.of(2000, 5, 15))
                .email("carlos@email.com")
                .matricula(1234)
                .build();

        assertEquals("Carlos", aluno.getNome());
        assertEquals(1234, aluno.getMatricula());
        assertEquals("carlos@email.com", aluno.getEmail());
    }

    @Test
    void infosDeveConterMatricula() {
        Aluno aluno = new Aluno.Builder()
                .nome("Ana")
                .dataNascimento(LocalDate.of(1999, 3, 20))
                .email("ana@email.com")
                .matricula(5678)
                .build();

        assertTrue(aluno.infos().contains("5678"));
        assertTrue(aluno.infos().contains("Matricula"));
    }
}