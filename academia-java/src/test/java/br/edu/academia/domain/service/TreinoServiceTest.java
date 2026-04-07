package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Treino;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.testutil.TestStubs.InMemoryTreinoRepository;
import br.edu.academia.testutil.TestStubs.NoOpLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreinoServiceTest {

    private TreinoService service;
    private InMemoryTreinoRepository repository;
    private HistoricoOperacoes historico;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTreinoRepository();
        historico  = new HistoricoOperacoes();
        service    = new TreinoService(repository, historico, new NoOpLogger());
    }

    @Test
    void deveCadastrarTreino() {
        Treino treino = service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);

        assertEquals("Peitoral", treino.getGrupoMuscular());
        assertEquals("Supino", treino.getExercicio());
        assertEquals(12, treino.getRepeticoes());
        assertEquals(60.0, treino.getPeso());
        assertEquals(1L, treino.getAlunoId());
        assertNotNull(treino.getData());
        assertTrue(treino.getId() > 0);
    }

    @Test
    void deveAtualizarTreino() {
        Treino treino = service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);

        Treino atualizado = service.atualizar(treino.getId(), "Costas", "Remada", 10, 80.0);

        assertEquals("Costas", atualizado.getGrupoMuscular());
        assertEquals("Remada", atualizado.getExercicio());
        assertEquals(10, atualizado.getRepeticoes());
        assertEquals(80.0, atualizado.getPeso());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(999L, "Costas", "Remada", 10, 80.0));
    }

    @Test
    void deveDeletarTreino() {
        Treino treino = service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);

        service.deletar(treino.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deveLancarExcecaoAoDeletarInexistente() {
        assertThrows(IllegalArgumentException.class, () -> service.deletar(999L));
    }

    @Test
    void deveListarPorAluno() {
        service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);
        service.cadastrar(1L, "Costas", "Remada", 10, 80.0);
        service.cadastrar(2L, "Pernas", "Agachamento", 15, 100.0);

        List<Treino> treinos = service.listarPorAluno(1L);

        assertEquals(2, treinos.size());
    }

    @Test
    void deveBuscarPorId() {
        Treino treino = service.cadastrar(1L, "Biceps", "Rosca", 15, 20.0);

        assertTrue(service.buscarPorId(treino.getId()).isPresent());
        assertTrue(service.buscarPorId(999L).isEmpty());
    }

    @Test
    void deveListarTodos() {
        service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);
        service.cadastrar(2L, "Costas", "Remada", 10, 80.0);

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void devePreservarAlunoIdNaAtualizacao() {
        Treino treino = service.cadastrar(42L, "Peitoral", "Supino", 12, 60.0);

        Treino atualizado = service.atualizar(treino.getId(), "Costas", "Remada", 10, 80.0);

        assertEquals(42L, atualizado.getAlunoId());
    }

    @Test
    void devePreservarDataNaAtualizacao() {
        Treino treino = service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);
        LocalDate dataCriacao = treino.getData();

        Treino atualizado = service.atualizar(treino.getId(), "Costas", "Remada", 10, 80.0);

        assertEquals(dataCriacao, atualizado.getData());
    }

    @Test
    void deveRegistrarMementoCriacao() {
        service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);

        assertEquals(1, historico.tamanho());
    }

    @Test
    void deveRegistrarMementoAtualizacao() {
        Treino treino = service.cadastrar(1L, "Peitoral", "Supino", 12, 60.0);
        service.atualizar(treino.getId(), "Costas", "Remada", 10, 80.0);

        assertEquals(2, historico.tamanho());
    }
}
