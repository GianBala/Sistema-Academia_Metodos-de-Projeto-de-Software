package br.edu.academia.domain.command;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.repository.AlunoRepository;
import br.edu.academia.domain.service.AcessoService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.domain.service.MatriculaGenerator;
import br.edu.academia.domain.repository.RegistroAcessoRepository;
import br.edu.academia.domain.entity.RegistroAcesso;
import br.edu.academia.infrastructure.log.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    private AlunoService alunoService;
    private InMemoryAlunoRepository alunoRepo;
    private AcessoService acessoService;
    private Logger logger;

    @BeforeEach
    void setUp() {
        alunoRepo = new InMemoryAlunoRepository();
        MatriculaGenerator generator = () -> 1234;
        alunoService = new AlunoService(alunoRepo, generator);
        acessoService = new AcessoService(new InMemoryRegistroAcessoRepository());
        logger = new NoOpLogger();
    }

    @Test
    void cadastrarAlunoCommandDeveExecutarEGuardarResultado() {
        CadastrarAlunoCommand cmd = new CadastrarAlunoCommand(
                alunoService, logger, acessoService,
                "Maria Silva", "15/03/2000", "maria@email.com");

        cmd.execute();

        assertNotNull(cmd.getResultado());
        assertEquals("Maria Silva", cmd.getResultado().getNome());
        assertEquals(1, alunoRepo.findAll().size());
    }

    @Test
    void listarAlunosCommandDeveRetornarLista() {
        alunoService.cadastrar("Aluno 1", "01/01/2000", "a1@e.com");
        alunoService.cadastrar("Aluno 2", "02/02/2001", "a2@e.com");

        ListarAlunosCommand cmd = new ListarAlunosCommand(alunoService, logger, acessoService);
        cmd.execute();

        assertEquals(2, cmd.getResultado().size());
    }

    @Test
    void atualizarAlunoCommandDeveExecutarEPermitirUndo() {
        Aluno aluno = alunoService.cadastrar("Maria Silva", "15/03/2000", "maria@email.com");
        long id = aluno.getId();

        AtualizarAlunoCommand cmd = new AtualizarAlunoCommand(
                alunoService, alunoRepo, logger, acessoService,
                id, "Maria Santos", "20/04/1999", "santos@email.com");

        cmd.execute();

        Aluno atualizado = alunoRepo.findById(id).orElseThrow();
        assertEquals("Maria Santos", atualizado.getNome());
        assertEquals("santos@email.com", atualizado.getEmail());

        cmd.undo();

        Aluno restaurado = alunoRepo.findById(id).orElseThrow();
        assertEquals("Maria Silva", restaurado.getNome());
        assertEquals("maria@email.com", restaurado.getEmail());
    }

    @Test
    void commandHistoryDeveGuardarEDesfazerUltimaAtualizacao() {
        Aluno aluno = alunoService.cadastrar("Carlos Lima", "10/10/1990", "carlos@email.com");
        long id = aluno.getId();

        CommandHistory history = new CommandHistory();

        AtualizarAlunoCommand cmd = new AtualizarAlunoCommand(
                alunoService, alunoRepo, logger, acessoService,
                id, "Carlos Novo", "11/11/1991", "novo@email.com");

        history.executeCommand(cmd);
        assertTrue(history.temAtualizacaoParaDesfazer());

        history.undoUltimaAtualizacao();
        assertFalse(history.temAtualizacaoParaDesfazer());

        Aluno restaurado = alunoRepo.findById(id).orElseThrow();
        assertEquals("Carlos Lima", restaurado.getNome());
    }

    @Test
    void commandHistoryNaoGuardaComandosDeCadastro() {
        CommandHistory history = new CommandHistory();

        CadastrarAlunoCommand cmd = new CadastrarAlunoCommand(
                alunoService, logger, acessoService,
                "Teste", "01/01/2000", "teste@email.com");

        history.executeCommand(cmd);
        assertFalse(history.temAtualizacaoParaDesfazer());
    }

    @Test
    void undoSemAtualizacaoDeveLancarExcecao() {
        CommandHistory history = new CommandHistory();
        assertThrows(IllegalStateException.class, history::undoUltimaAtualizacao);
    }

    // --- helpers ---

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
        public void update(Aluno aluno) {
            for (int i = 0; i < alunos.size(); i++) {
                if (alunos.get(i).getId() == aluno.getId()) {
                    alunos.set(i, aluno);
                    return;
                }
            }
        }
    }

    static class InMemoryRegistroAcessoRepository implements RegistroAcessoRepository {
        private final List<RegistroAcesso> registros = new ArrayList<>();
        private long nextId = 1;

        @Override
        public void save(RegistroAcesso registro) {
            registro.setId(nextId++);
            registros.add(registro);
        }

        @Override
        public List<RegistroAcesso> findAll() {
            return new ArrayList<>(registros);
        }

        @Override
        public Optional<RegistroAcesso> findById(long id) {
            return registros.stream().filter(r -> r.getId() == id).findFirst();
        }

        @Override
        public List<RegistroAcesso> findByTipoOperacao(String tipoOperacao) {
            return registros.stream().filter(r -> r.getTipoOperacao().equals(tipoOperacao))
                    .collect(Collectors.toList());
        }

        @Override
        public List<RegistroAcesso> findByTipoEntidade(String tipoEntidade) {
            return registros.stream().filter(r -> r.getTipoEntidade().equals(tipoEntidade))
                    .collect(Collectors.toList());
        }

        @Override
        public long countByTipoOperacaoAndTipoEntidade(String tipoOperacao, String tipoEntidade) {
            return registros.stream()
                    .filter(r -> r.getTipoOperacao().equals(tipoOperacao)
                            && r.getTipoEntidade().equals(tipoEntidade))
                    .count();
        }

        @Override
        public void update(RegistroAcesso entity) {
            throw new UnsupportedOperationException();
        }
    }

    static class NoOpLogger implements Logger {
        @Override
        public void info(String message) {}

        @Override
        public void warn(String message) {}

        @Override
        public void error(String message) {}
    }
}
