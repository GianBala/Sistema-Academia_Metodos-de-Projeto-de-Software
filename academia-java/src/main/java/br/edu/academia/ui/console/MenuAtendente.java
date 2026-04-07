package br.edu.academia.ui.console;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.service.SessionContext;
import br.edu.academia.ui.console.command.*;

import java.util.List;

/**
 * Menu do Atendente — CRUD de alunos e professores, delegação de alunos a turmas.
 * Não pode gerenciar outros atendentes nem administradores.
 */
public class MenuAtendente {

    private final AcademiaFacade facade;
    private final HistoricoOperacoes historico;
    private final ConsoleUtils console;

    public MenuAtendente(AcademiaFacade facade, HistoricoOperacoes historico, ConsoleUtils console) {
        this.facade = facade;
        this.historico = historico;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Menu Atendente - " + SessionContext.getCurrentUser().getNome());
            System.out.println("=== ALUNOS ===");
            System.out.println("1. Cadastrar Aluno");
            System.out.println("2. Listar Alunos");
            System.out.println("3. Atualizar Aluno");
            System.out.println("4. Deletar Aluno");
            System.out.println("5. Alocar Aluno em Turma");
            System.out.println("=== PROFESSORES ===");
            System.out.println("6. Cadastrar Professor");
            System.out.println("7. Listar Professores");
            System.out.println("8. Atualizar Professor");
            System.out.println("9. Deletar Professor");
            System.out.println("=== TURMAS ===");
            System.out.println("10. Listar Turmas");
            System.out.println("=== SISTEMA ===");
            System.out.println("11. Desfazer ultima operacao");
            System.out.println("0.  Sair / Logout");
            System.out.println();

            String escolha = console.readInput("Opcao: ");
            try {
                switch (escolha) {
                    case "1"  -> { new CadastrarAlunoCommand(facade, console).execute(); console.waitForEnter(); }
                    case "2"  -> listarAlunos();
                    case "3"  -> atualizarAluno();
                    case "4"  -> deletarAluno();
                    case "5"  -> alocarAlunoTurma();
                    case "6"  -> { new CadastrarProfessorCommand(facade, console).execute(); console.waitForEnter(); }
                    case "7"  -> listarProfessores();
                    case "8"  -> atualizarProfessor();
                    case "9"  -> deletarProfessor();
                    case "10" -> listarTurmas();
                    case "11" -> new DesfazerCommand(historico, console).execute();
                    case "0"  -> { SessionContext.clearSession(); running = false; }
                    default   -> { System.out.println("Opcao invalida."); console.waitForEnter(); }
                }
            } catch (SecurityException e) {
                System.out.println("Acesso negado: " + e.getMessage());
                console.waitForEnter();
            }
        }
    }

    private void listarAlunos() {
        List<Aluno> alunos = facade.listarAlunos();
        if (alunos.isEmpty()) System.out.println("Nenhum aluno cadastrado.");
        else alunos.forEach(a -> System.out.println(a.infos()));
        console.waitForEnter();
    }

    private void atualizarAluno() {
        try {
            long id = Long.parseLong(console.readInput("ID do aluno: "));
            String nome  = console.readInput("Novo nome: ");
            String data  = console.readInput("Nova data (dd/mm/aaaa): ");
            String email = console.readInput("Novo email: ");
            String login = console.readInput("Novo login: ");
            String senha = console.readInput("Nova senha: ");
            Aluno a = facade.atualizarAluno(id, nome, data, email, login, senha);
            System.out.println("Aluno atualizado: " + a.getNome());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void deletarAluno() {
        try {
            long id = Long.parseLong(console.readInput("ID do aluno: "));
            facade.deletarAluno(id);
            System.out.println("Aluno deletado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void alocarAlunoTurma() {
        try {
            long alunoId = Long.parseLong(console.readInput("ID do aluno: "));
            System.out.println("Turmas disponíveis:");
            facade.listarTurmas().forEach(t -> System.out.println("  " + t.infos()));
            long turmaId = Long.parseLong(console.readInput("ID da turma: "));
            facade.adicionarAlunoATurma(turmaId, alunoId);
            System.out.println("Aluno alocado a turma com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void listarProfessores() {
        List<Professor> professores = facade.listarProfessores();
        if (professores.isEmpty()) System.out.println("Nenhum professor cadastrado.");
        else professores.forEach(p -> System.out.println(p.infos()));
        console.waitForEnter();
    }

    private void atualizarProfessor() {
        try {
            long id = Long.parseLong(console.readInput("ID do professor: "));
            String nome  = console.readInput("Novo nome: ");
            String data  = console.readInput("Nova data (dd/mm/aaaa): ");
            String email = console.readInput("Novo email: ");
            String login = console.readInput("Novo login: ");
            String senha = console.readInput("Nova senha: ");
            Professor p = facade.atualizarProfessor(id, nome, data, email, login, senha);
            System.out.println("Professor atualizado: " + p.getNome());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void deletarProfessor() {
        try {
            long id = Long.parseLong(console.readInput("ID do professor: "));
            facade.deletarProfessor(id);
            System.out.println("Professor deletado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void listarTurmas() {
        facade.listarTurmas().forEach(t -> System.out.println(t.infos()));
        console.waitForEnter();
    }
}
