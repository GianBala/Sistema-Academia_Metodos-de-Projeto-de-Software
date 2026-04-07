package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    @Test
    void deveCriarProfessorComBuilder() {
        Professor professor = new Professor.Builder()
                .nome("Prof Carlos")
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .email("prof@email.com")
                .login("profcarlos")
                .senhaHash("hash")
                .build();

        assertEquals("Prof Carlos", professor.getNome());
        assertEquals("prof@email.com", professor.getEmail());
        assertEquals("profcarlos", professor.getLogin());
    }

    @Test
    void infosDeveConterProfessor() {
        Professor professor = new Professor.Builder()
                .nome("Dr Ana")
                .dataNascimento(LocalDate.of(1975, 6, 10))
                .email("ana@email.com")
                .login("profdrana")
                .senhaHash("hash")
                .build();

        String infos = professor.infos();

        assertTrue(infos.contains("Dr Ana"));
        assertTrue(infos.contains("Professor"));
    }
}
