package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Aluno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SqliteAlunoRepositoryTest {

    private DatabaseConnection db;
    private SqliteAlunoRepository repository;

    @BeforeEach
    void setUp() {
        db = new DatabaseConnection("jdbc:sqlite::memory:");
        repository = new SqliteAlunoRepository(db.getConnection());
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void deveSalvarERecuperarAluno() {
        Aluno aluno = new Aluno("Maria", LocalDate.of(2000, 3, 15), "maria@email.com", 1234);

        repository.save(aluno);

        assertTrue(aluno.getId() > 0);
        List<Aluno> todos = repository.findAll();
        assertEquals(1, todos.size());
        assertEquals("Maria", todos.get(0).getNome());
        assertEquals(1234, todos.get(0).getMatricula());
    }

    @Test
    void deveBuscarPorId() {
        Aluno aluno = new Aluno("Carlos", LocalDate.of(1999, 1, 1), "carlos@email.com", 5678);
        repository.save(aluno);

        Optional<Aluno> encontrado = repository.findById(aluno.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Carlos", encontrado.get().getNome());
    }

    @Test
    void deveBuscarPorMatricula() {
        Aluno aluno = new Aluno("Ana", LocalDate.of(2001, 6, 10), "ana@email.com", 9999);
        repository.save(aluno);

        Optional<Aluno> encontrado = repository.findByMatricula(9999);

        assertTrue(encontrado.isPresent());
        assertEquals("Ana", encontrado.get().getNome());
    }

    @Test
    void deveRetornarVazioParaMatriculaInexistente() {
        Optional<Aluno> encontrado = repository.findByMatricula(0);

        assertFalse(encontrado.isPresent());
    }

    @Test
    void deveListarMultiplosAlunos() {
        repository.save(new Aluno("A", LocalDate.of(2000, 1, 1), "a@e.com", 1));
        repository.save(new Aluno("B", LocalDate.of(2000, 1, 1), "b@e.com", 2));
        repository.save(new Aluno("C", LocalDate.of(2000, 1, 1), "c@e.com", 3));

        assertEquals(3, repository.findAll().size());
    }
}
