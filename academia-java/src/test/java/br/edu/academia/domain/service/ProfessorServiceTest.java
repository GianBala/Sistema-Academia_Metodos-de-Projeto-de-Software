package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorServiceTest {

    private ProfessorService service;
    private InMemoryProfessorRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryProfessorRepository();
        service = new ProfessorService(repository);
    }

    @Test
    void deveCadastrarProfessorMaiorDe18() {
        String dataNascimento = LocalDate.now().minusYears(30).format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Professor prof = service.cadastrar("Carlos", dataNascimento, "carlos@email.com");

        assertEquals("Carlos", prof.getNome());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveRejeitarProfessorMenorDe18() {
        String dataNascimento = LocalDate.now().minusYears(17).format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Jovem", dataNascimento, "jovem@email.com"));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        String dataNascimento = LocalDate.now().minusYears(30).format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Carlos", dataNascimento, "invalido"));
    }

    static class InMemoryProfessorRepository implements ProfessorRepository {
        private final List<Professor> professores = new ArrayList<>();
        private long nextId = 1;

        @Override
        public void save(Professor professor) {
            professor.setId(nextId++);
            professores.add(professor);
        }

        @Override
        public List<Professor> findAll() {
            return new ArrayList<>(professores);
        }

        @Override
        public Optional<Professor> findById(long id) {
            return professores.stream().filter(p -> p.getId() == id).findFirst();
        }
    }
}
