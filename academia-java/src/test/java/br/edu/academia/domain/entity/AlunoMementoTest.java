package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AlunoMementoTest {

    @Test
    void deveCriarMementoComEstadoAtual() {
        Aluno aluno = new Aluno("Maria Silva", LocalDate.of(2000, 3, 15), "maria@email.com", 1234);
        aluno.setId(1L);

        AlunoMemento memento = aluno.criarMemento();

        assertEquals(1L, memento.getId());
        assertEquals("Maria Silva", memento.getNome());
        assertEquals(LocalDate.of(2000, 3, 15), memento.getDataNascimento());
        assertEquals("maria@email.com", memento.getEmail());
        assertEquals(1234, memento.getMatricula());
    }

    @Test
    void deveRestaurarEstadoAPartirDoMemento() {
        Aluno aluno = new Aluno("Maria Silva", LocalDate.of(2000, 3, 15), "maria@email.com", 1234);
        aluno.setId(1L);

        AlunoMemento memento = aluno.criarMemento();

        aluno.setNome("Outro Nome");
        aluno.setEmail("outro@email.com");
        aluno.setDataNascimento(LocalDate.of(1990, 1, 1));

        aluno.restaurarMemento(memento);

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals("maria@email.com", aluno.getEmail());
        assertEquals(LocalDate.of(2000, 3, 15), aluno.getDataNascimento());
        assertEquals(1234, aluno.getMatricula());
    }

    @Test
    void mementoDeveSerImutavel() {
        Aluno aluno = new Aluno("Maria Silva", LocalDate.of(2000, 3, 15), "maria@email.com", 1234);
        aluno.setId(1L);

        AlunoMemento memento = aluno.criarMemento();

        aluno.setNome("Nome Modificado");

        assertEquals("Maria Silva", memento.getNome());
    }
}
