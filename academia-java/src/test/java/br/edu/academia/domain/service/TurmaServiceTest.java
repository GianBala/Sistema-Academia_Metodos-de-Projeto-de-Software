package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Turma;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.testutil.TestStubs.InMemoryTurmaRepository;
import br.edu.academia.testutil.TestStubs.NoOpLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurmaServiceTest {

    private TurmaService service;
    private InMemoryTurmaRepository repository;
    private HistoricoOperacoes historico;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTurmaRepository();
        historico  = new HistoricoOperacoes();
        service    = new TurmaService(repository, historico, new NoOpLogger());
    }

    @Test
    void deveCadastrarTurma() {
        Turma turma = service.cadastrar("Musculacao A", 1L, "08:00");

        assertEquals("Musculacao A", turma.getNome());
        assertEquals(1L, turma.getProfessorId());
        assertEquals("08:00", turma.getHorario());
        assertTrue(turma.getId() > 0);
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveLancarExcecaoParaNomeVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("", 1L, "08:00"));
    }

    @Test
    void deveLancarExcecaoParaHorarioVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Turma A", 1L, ""));
    }

    @Test
    void deveAtualizarTurma() {
        Turma turma = service.cadastrar("Original", 1L, "08:00");

        Turma atualizada = service.atualizar(turma.getId(), "Novo Nome", 2L, "10:00");

        assertEquals("Novo Nome", atualizada.getNome());
        assertEquals(2L, atualizada.getProfessorId());
        assertEquals("10:00", atualizada.getHorario());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(999L, "Nome", 1L, "08:00"));
    }

    @Test
    void deveDeletarTurma() {
        Turma turma = service.cadastrar("Para Deletar", 1L, "08:00");

        service.deletar(turma.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deveLancarExcecaoAoDeletarInexistente() {
        assertThrows(IllegalArgumentException.class, () -> service.deletar(999L));
    }

    @Test
    void deveAdicionarAlunoATurma() {
        Turma turma = service.cadastrar("Turma A", 1L, "08:00");

        service.adicionarAluno(turma.getId(), 10L);
        service.adicionarAluno(turma.getId(), 20L);

        List<Long> alunos = service.listarAlunosDaTurma(turma.getId());
        assertEquals(2, alunos.size());
        assertTrue(alunos.contains(10L));
        assertTrue(alunos.contains(20L));
    }

    @Test
    void deveLancarExcecaoAoAdicionarAlunoEmTurmaInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarAluno(999L, 10L));
    }

    @Test
    void deveRemoverAlunoDaTurma() {
        Turma turma = service.cadastrar("Turma B", 1L, "09:00");
        service.adicionarAluno(turma.getId(), 10L);
        service.adicionarAluno(turma.getId(), 20L);

        service.removerAluno(turma.getId(), 10L);

        List<Long> alunos = service.listarAlunosDaTurma(turma.getId());
        assertEquals(1, alunos.size());
        assertFalse(alunos.contains(10L));
    }

    @Test
    void deveListarTurmasDoProfessor() {
        service.cadastrar("Turma P1-A", 1L, "08:00");
        service.cadastrar("Turma P1-B", 1L, "10:00");
        service.cadastrar("Turma P2-A", 2L, "09:00");

        List<Turma> turmasDoProfUm = service.listarTurmasDoProfessor(1L);

        assertEquals(2, turmasDoProfUm.size());
    }

    @Test
    void deveListarTodos() {
        service.cadastrar("T1", 1L, "08:00");
        service.cadastrar("T2", 2L, "09:00");

        assertEquals(2, service.listarTodos().size());
    }

    @Test
    void deveBuscarPorId() {
        Turma turma = service.cadastrar("Busca", 1L, "08:00");

        assertTrue(service.buscarPorId(turma.getId()).isPresent());
        assertTrue(service.buscarPorId(999L).isEmpty());
    }

    @Test
    void deveRegistrarMementoCriacao() {
        service.cadastrar("Musculacao", 1L, "08:00");

        assertEquals(1, historico.tamanho());
    }

    @Test
    void deveRegistrarMementoAtualizacao() {
        Turma turma = service.cadastrar("Original", 1L, "08:00");
        service.atualizar(turma.getId(), "Novo", 1L, "09:00");

        assertEquals(2, historico.tamanho());
    }
}
