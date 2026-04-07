package br.edu.academia.domain.facade;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.facade.command.FacadeCommand;
import br.edu.academia.domain.service.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Padrão Facade + Singleton.
 *
 * Fornece uma interface simplificada a todo o subsistema de serviços.
 * Cada operação cria e executa um FacadeCommand internamente (padrão Command).
 *
 * Uso: AcademiaFacade.init(...) uma vez em App.java,
 *      depois AcademiaFacade.getInstance() em qualquer lugar.
 */
public class AcademiaFacade {

    private static volatile AcademiaFacade instance;

    private AlunoService alunoService;
    private ProfessorService professorService;
    private AtendenteService atendenteService;
    private AdministradorService administradorService;
    private TurmaService turmaService;
    private TreinoService treinoService;

    private AcademiaFacade() {}

    /** Inicializa a instância única com os serviços necessários. */
    public static void init(AlunoService alunoService,
                            ProfessorService professorService,
                            AtendenteService atendenteService,
                            AdministradorService administradorService,
                            TurmaService turmaService,
                            TreinoService treinoService) {
        if (instance == null) {
            synchronized (AcademiaFacade.class) {
                if (instance == null) {
                    AcademiaFacade f = new AcademiaFacade();
                    f.alunoService = alunoService;
                    f.professorService = professorService;
                    f.atendenteService = atendenteService;
                    f.administradorService = administradorService;
                    f.turmaService = turmaService;
                    f.treinoService = treinoService;
                    instance = f;
                }
            }
        }
    }

    public static AcademiaFacade getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AcademiaFacade não inicializada. Chame init() primeiro.");
        }
        return instance;
    }

    // ── Contagem total de entidades (exigida pelo professor) ───────────────────

    /** Retorna o número total de entidades cadastradas no sistema. */
    public long contarEntidades() {
        FacadeCommand<Long> cmd = () ->
                (long) alunoService.listarTodos().size()
                + professorService.listarTodos().size()
                + atendenteService.listarTodos().size()
                + administradorService.listarTodos().size();
        return cmd.execute();
    }

    /** Retorna contagem por tipo de entidade. */
    public Map<String, Integer> contarEntidadesPorTipo() {
        FacadeCommand<Map<String, Integer>> cmd = () -> {
            Map<String, Integer> counts = new LinkedHashMap<>();
            counts.put("Alunos", alunoService.listarTodos().size());
            counts.put("Professores", professorService.listarTodos().size());
            counts.put("Atendentes", atendenteService.listarTodos().size());
            counts.put("Administradores", administradorService.listarTodos().size());
            counts.put("Turmas", turmaService.listarTodos().size());
            return counts;
        };
        return cmd.execute();
    }

    // ── Alunos ─────────────────────────────────────────────────────────────────

    public Aluno cadastrarAluno(String nome, String data, String email, String login, String senha) {
        FacadeCommand<Aluno> cmd = () -> alunoService.cadastrar(nome, data, email, login, senha);
        return cmd.execute();
    }

    public Aluno atualizarAluno(long id, String nome, String data, String email, String login, String senha) {
        FacadeCommand<Aluno> cmd = () -> alunoService.atualizar(id, nome, data, email, login, senha);
        return cmd.execute();
    }

    public void deletarAluno(long id) {
        FacadeCommand<Void> cmd = () -> { alunoService.deletar(id); return null; };
        cmd.execute();
    }

    public List<Aluno> listarAlunos() {
        FacadeCommand<List<Aluno>> cmd = alunoService::listarTodos;
        return cmd.execute();
    }

    public Optional<Aluno> buscarAluno(long id) {
        FacadeCommand<Optional<Aluno>> cmd = () -> alunoService.buscarPorId(id);
        return cmd.execute();
    }

    // ── Professores ────────────────────────────────────────────────────────────

    public Professor cadastrarProfessor(String nome, String data, String email, String login, String senha) {
        FacadeCommand<Professor> cmd = () -> professorService.cadastrar(nome, data, email, login, senha);
        return cmd.execute();
    }

    public Professor atualizarProfessor(long id, String nome, String data, String email, String login, String senha) {
        FacadeCommand<Professor> cmd = () -> professorService.atualizar(id, nome, data, email, login, senha);
        return cmd.execute();
    }

    public void deletarProfessor(long id) {
        FacadeCommand<Void> cmd = () -> { professorService.deletar(id); return null; };
        cmd.execute();
    }

    public List<Professor> listarProfessores() {
        FacadeCommand<List<Professor>> cmd = professorService::listarTodos;
        return cmd.execute();
    }

    // ── Atendentes ─────────────────────────────────────────────────────────────

    public Atendente cadastrarAtendente(String nome, String data, String email, String login, String senha) {
        FacadeCommand<Atendente> cmd = () -> atendenteService.cadastrar(nome, data, email, login, senha);
        return cmd.execute();
    }

    public Atendente atualizarAtendente(long id, String nome, String data, String email, String login, String senha) {
        FacadeCommand<Atendente> cmd = () -> atendenteService.atualizar(id, nome, data, email, login, senha);
        return cmd.execute();
    }

    public void deletarAtendente(long id) {
        FacadeCommand<Void> cmd = () -> { atendenteService.deletar(id); return null; };
        cmd.execute();
    }

    public List<Atendente> listarAtendentes() {
        FacadeCommand<List<Atendente>> cmd = atendenteService::listarTodos;
        return cmd.execute();
    }

    // ── Administradores ────────────────────────────────────────────────────────

    public Administrador cadastrarAdministrador(String nome, String data, String email, String login, String senha) {
        FacadeCommand<Administrador> cmd = () -> administradorService.cadastrar(nome, data, email, login, senha);
        return cmd.execute();
    }

    public Administrador atualizarAdministrador(long id, String nome, String data, String email, String login, String senha) {
        FacadeCommand<Administrador> cmd = () -> administradorService.atualizar(id, nome, data, email, login, senha);
        return cmd.execute();
    }

    public void deletarAdministrador(long id) {
        FacadeCommand<Void> cmd = () -> { administradorService.deletar(id); return null; };
        cmd.execute();
    }

    public List<Administrador> listarAdministradores() {
        FacadeCommand<List<Administrador>> cmd = administradorService::listarTodos;
        return cmd.execute();
    }

    // ── Turmas ─────────────────────────────────────────────────────────────────

    public Turma cadastrarTurma(String nome, long professorId, String horario) {
        FacadeCommand<Turma> cmd = () -> turmaService.cadastrar(nome, professorId, horario);
        return cmd.execute();
    }

    public Turma atualizarTurma(long id, String nome, long professorId, String horario) {
        FacadeCommand<Turma> cmd = () -> turmaService.atualizar(id, nome, professorId, horario);
        return cmd.execute();
    }

    public void deletarTurma(long id) {
        FacadeCommand<Void> cmd = () -> { turmaService.deletar(id); return null; };
        cmd.execute();
    }

    public List<Turma> listarTurmas() {
        FacadeCommand<List<Turma>> cmd = turmaService::listarTodos;
        return cmd.execute();
    }

    public void adicionarAlunoATurma(long turmaId, long alunoId) {
        FacadeCommand<Void> cmd = () -> { turmaService.adicionarAluno(turmaId, alunoId); return null; };
        cmd.execute();
    }

    public List<Turma> listarTurmasDoProfessor(long professorId) {
        FacadeCommand<List<Turma>> cmd = () -> turmaService.listarTurmasDoProfessor(professorId);
        return cmd.execute();
    }

    public List<Long> listarAlunosDaTurma(long turmaId) {
        FacadeCommand<List<Long>> cmd = () -> turmaService.listarAlunosDaTurma(turmaId);
        return cmd.execute();
    }

    // ── Treinos ────────────────────────────────────────────────────────────────

    public Treino cadastrarTreino(long alunoId, String grupoMuscular, String exercicio,
                                   int repeticoes, double peso) {
        FacadeCommand<Treino> cmd = () -> treinoService.cadastrar(alunoId, grupoMuscular, exercicio, repeticoes, peso);
        return cmd.execute();
    }

    public Treino atualizarTreino(long id, String grupoMuscular, String exercicio,
                                   int repeticoes, double peso) {
        FacadeCommand<Treino> cmd = () -> treinoService.atualizar(id, grupoMuscular, exercicio, repeticoes, peso);
        return cmd.execute();
    }

    public void deletarTreino(long id) {
        FacadeCommand<Void> cmd = () -> { treinoService.deletar(id); return null; };
        cmd.execute();
    }

    public List<Treino> listarTreinosDoAluno(long alunoId) {
        FacadeCommand<List<Treino>> cmd = () -> treinoService.listarPorAluno(alunoId);
        return cmd.execute();
    }
}
