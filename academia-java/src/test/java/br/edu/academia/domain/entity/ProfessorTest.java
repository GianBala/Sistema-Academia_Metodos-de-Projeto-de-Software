package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void deveAdicionarAlunoAoProfessor() {
        Professor professor = new Professor.Builder()
                .nome("Prof Carlos")
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .email("prof@email.com")
                .build();

        Aluno aluno = new Aluno.Builder()
                .nome("Aluno Teste")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .email("aluno@email.com")
                .matricula(1)
                .build();

        professor.adicionarAluno(aluno);

        assertEquals(1, professor.getAlunos().size());
        assertEquals("Aluno Teste", professor.getAlunos().get(0).getNome());
    }

    @Test
    void listaDeAlunosDeveSerImutavel() {
        Professor professor = new Professor.Builder()
                .nome("Prof")
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .email("prof@email.com")
                .build();

        Aluno aluno = new Aluno.Builder()
                .nome("X")
                .dataNascimento(LocalDate.now())
                .email("x@e.com")
                .matricula(1)
                .build();

        assertThrows(UnsupportedOperationException.class, () ->
                professor.getAlunos().add(aluno));
    }
}