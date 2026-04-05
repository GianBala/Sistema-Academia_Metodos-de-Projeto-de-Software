package br.edu.academia.domain.memento;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.repository.AlunoRepository;
import br.edu.academia.domain.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HistoricoOperacoesTest {

    private HistoricoOperacoes historico;
    private InMemoryAlunoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAlunoRepository();
        Map<TipoEntidade, Repository<?>> repos = Map.of(TipoEntidade.ALUNO, repository);
        historico = new HistoricoOperacoes(repos);
    }

    @Test
    void deveRetornarFalseQuandoHistoricoVazio() {
        assertFalse(historico.temHistorico());
    }

    @Test
    void deveRetornarTrueAposRegistrar() {
        historico.registrar(new CadastroMemento(1L, TipoEntidade.ALUNO, "Aluno: Maria"));
        assertTrue(historico.temHistorico());
    }

    @Test
    void deveRetornarOptionalVazioAoDesfazerSemHistorico() {
        Optional<CadastroMemento> resultado = historico.desfazer();
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveDesfazerCadastroEDeletarEntidade() {
        repository.save(criarAlunoComId(1L));
        assertEquals(1, repository.findAll().size());

        historico.registrar(new CadastroMemento(1L, TipoEntidade.ALUNO, "Aluno: Maria"));
        Optional<CadastroMemento> resultado = historico.desfazer();

        assertTrue(resultado.isPresent());
        assertEquals("Aluno: Maria", resultado.get().getDescricao());
        assertEquals(0, repository.findAll().size());
        assertFalse(historico.temHistorico());
    }

    @Test
    void deveDesfazerEmOrdemLIFO() {
        repository.save(criarAlunoComId(1L));
        repository.save(criarAlunoComId(2L));

        historico.registrar(new CadastroMemento(1L, TipoEntidade.ALUNO, "Aluno: Primeiro"));
        historico.registrar(new CadastroMemento(2L, TipoEntidade.ALUNO, "Aluno: Segundo"));

        Optional<CadastroMemento> primeiro = historico.desfazer();
        assertTrue(primeiro.isPresent());
        assertEquals("Aluno: Segundo", primeiro.get().getDescricao());

        Optional<CadastroMemento> segundo = historico.desfazer();
        assertTrue(segundo.isPresent());
        assertEquals("Aluno: Primeiro", segundo.get().getDescricao());

        assertEquals(0, repository.findAll().size());
    }

    private Aluno criarAlunoComId(long id) {
        Aluno aluno = new Aluno.Builder()
                .nome("Aluno " + id)
                .dataNascimento(java.time.LocalDate.of(2000, 1, 1))
                .email("aluno" + id + "@email.com")
                .matricula((int) id)
                .build();
        aluno.setId(id);
        return aluno;
    }

    static class InMemoryAlunoRepository implements AlunoRepository {
        private final List<Aluno> alunos = new ArrayList<>();

        public void save(Aluno aluno) {
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
