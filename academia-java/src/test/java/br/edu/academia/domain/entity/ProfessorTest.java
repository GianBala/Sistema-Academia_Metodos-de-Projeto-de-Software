package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void deveAdicionarAlunoAoProfessor() {
        Professor professor = new Professor("Prof Carlos", LocalDate.of(1980, 1, 1), "prof@email.com");
        Aluno aluno = new Aluno("Aluno Teste", LocalDate.of(2000, 1, 1), "aluno@email.com", 1);

        professor.adicionarAluno(aluno);

        assertEquals(1, professor.getAlunos().size());
        assertEquals("Aluno Teste", professor.getAlunos().get(0).getNome());
    }

    @Test
    void listaDeAlunosDeveSerImutavel() {
        Professor professor = new Professor("Prof", LocalDate.of(1980, 1, 1), "prof@email.com");

        assertThrows(UnsupportedOperationException.class, () ->
                professor.getAlunos().add(new Aluno("X", LocalDate.now(), "x@e.com", 1)));
    }
}
