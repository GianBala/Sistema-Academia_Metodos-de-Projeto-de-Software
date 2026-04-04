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
        Aluno aluno = new Aluno.Builder()
                .nome("Maria")
                .dataNascimento(LocalDate.of(2000, 3, 15))
                .email("maria@email.com")
                .matricula(1234)
                .build();

        repository.save(aluno);

        assertTrue(aluno.getId() > 0);
        List<Aluno> todos = repository.findAll();
        assertEquals(1, todos.size());
        assertEquals("Maria", todos.get(0).getNome());
        assertEquals(1234, todos.get(0).getMatricula());
    }

    @Test
    void deveBuscarPorId() {
        Aluno aluno = new Aluno.Builder()
                .nome("Carlos")
                .dataNascimento(LocalDate.of(1999, 1, 1))
                .email("carlos@email.com")
                .matricula(5678)
                .build();
        repository.save(aluno);

        Optional<Aluno> encontrado = repository.findById(aluno.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Carlos", encontrado.get().getNome());
    }

    @Test
    void deveBuscarPorMatricula() {
        Aluno aluno = new Aluno.Builder()
                .nome("Ana")
                .dataNascimento(LocalDate.of(2001, 6, 10))
                .email("ana@email.com")
                .matricula(9999)
                .build();
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
        repository.save(new Aluno.Builder().nome("A").dataNascimento(LocalDate.of(2000, 1, 1)).email("a@e.com").matricula(1).build());
        repository.save(new Aluno.Builder().nome("B").dataNascimento(LocalDate.of(2000, 1, 1)).email("b@e.com").matricula(2).build());
        repository.save(new Aluno.Builder().nome("C").dataNascimento(LocalDate.of(2000, 1, 1)).email("c@e.com").matricula(3).build());

        assertEquals(3, repository.findAll().size());
    }
}