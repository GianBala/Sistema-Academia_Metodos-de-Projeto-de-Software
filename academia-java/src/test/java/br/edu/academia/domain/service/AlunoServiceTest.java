package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AlunoServiceTest {

    private AlunoService service;
    private InMemoryAlunoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAlunoRepository();
        MatriculaGenerator generator = () -> 1234;
        var historico = new HistoricoOperacoes(Map.of(TipoEntidade.ALUNO, repository));
        service = new AlunoService(repository, generator, historico);
    }

    @Test
    void deveCadastrarAlunoComDadosValidos() {
        Aluno aluno = service.cadastrar("Maria Silva", "15/03/2000", "maria@email.com");

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals(1234, aluno.getMatricula());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveLancarExcecaoParaEmailInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Maria", "15/03/2000", "emailinvalido"));
    }

    @Test
    void deveLancarExcecaoParaDataInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Maria", "data-invalida", "maria@email.com"));
    }

    @Test
    void deveListarTodosOsAlunos() {
        service.cadastrar("Aluno 1", "01/01/2000", "a1@e.com");
        service.cadastrar("Aluno 2", "02/02/2000", "a2@e.com");

        assertEquals(2, service.listarTodos().size());
    }

    static class InMemoryAlunoRepository implements AlunoRepository {
        private final List<Aluno> alunos = new ArrayList<>();
        private long nextId = 1;

        @Override
        public void save(Aluno aluno) {
            aluno.setId(nextId++);
            alunos.add(aluno);
        }

        @Override
        public List<Aluno> findAll() {
            return new ArrayList<>(alunos);
        }

        @Override
        public Optional<Aluno> findById(long id) {
            return alunos.stream().filter(a -> a.getId() == id).findFirst();
        }

        @Override
        public Optional<Aluno> findByMatricula(int matricula) {
            return alunos.stream().filter(a -> a.getMatricula() == matricula).findFirst();
        }

        @Override
        public void delete(long id) {
            alunos.removeIf(a -> a.getId() == id);
        }
    }
}
