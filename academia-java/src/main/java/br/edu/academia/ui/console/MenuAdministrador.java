package br.edu.academia.ui.console;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.report.HTMLRelatorio;
import br.edu.academia.domain.report.PDFRelatorio;
import br.edu.academia.domain.repository.LoginHistoryRepository;
import br.edu.academia.domain.service.SessionContext;
import br.edu.academia.ui.console.command.*;

import java.util.List;
import java.util.Map;

/**
 * Menu do Administrador — acesso total a todas as operações.
 */
public class MenuAdministrador {

    private final AcademiaFacade facade;
    private final HistoricoOperacoes historico;
    private final LoginHistoryRepository loginHistory;
    private final ConsoleUtils console;

    public MenuAdministrador(AcademiaFacade facade,
                              HistoricoOperacoes historico,
                              LoginHistoryRepository loginHistory,
                              ConsoleUtils console) {
        this.facade = facade;
        this.historico = historico;
        this.loginHistory = loginHistory;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Menu Administrador - " + SessionContext.getCurrentUser().getNome());
            System.out.println("=== USUARIOS ===");
            System.out.println("1.  Cadastrar Administrador");
            System.out.println("2.  Cadastrar Professor");
            System.out.println("3.  Cadastrar Atendente");
            System.out.println("4.  Cadastrar Aluno");
            System.out.println("5.  Listar todos");
            System.out.println("6.  Atualizar usuario");
            System.out.println("7.  Deletar usuario");
            System.out.println("=== TURMAS / TREINOS ===");
            System.out.println("8.  Gerenciar Turmas");
            System.out.println("=== SISTEMA ===");
            System.out.println("9.  Gerar Relatorio HTML");
            System.out.println("10. Gerar Relatorio PDF");
            System.out.println("11. Desfazer ultima operacao");
            System.out.println("12. Estatisticas do sistema");
            System.out.println("0.  Sair / Logout");
            System.out.println();

            String escolha = console.readInput("Opcao: ");
            switch (escolha) {
                case "1" -> { new CadastrarAdministradorCommand(facade, console).execute(); console.waitForEnter(); }
                case "2" -> { new CadastrarProfessorCommand(facade, console).execute(); console.waitForEnter(); }
                case "3" -> { new CadastrarAtendenteCommand(facade, console).execute(); console.waitForEnter(); }
                case "4" -> { new CadastrarAlunoCommand(facade, console).execute(); console.waitForEnter(); }
                case "5" -> { new ListarUsuariosCommand(facade, console).execute(); console.waitForEnter(); }
                case "6" -> menuAtualizar();
                case "7" -> menuDeletar();
                case "8" -> menuTurmas();
                case "9" -> gerarRelatorio("HTML");
                case "10" -> gerarRelatorio("PDF");
                case "11" -> { new DesfazerCommand(historico, console).execute(); }
                case "12" -> mostrarEstatisticas();
                case "0" -> { SessionContext.clearSession(); running = false; }
                default  -> { System.out.println("Opcao invalida."); console.waitForEnter(); }
            }
        }
    }

    private void menuAtualizar() {
        console.clearConsole();
        console.printHeader("Atualizar Usuario");
        System.out.println("1. Aluno  2. Professor  3. Atendente  4. Administrador");
        String tipo = console.readInput("Tipo: ");
        String idStr = console.readInput("ID do usuario: ");
        try {
            long id = Long.parseLong(idStr);
            String nome  = console.readInput("Novo nome: ");
            String data  = console.readInput("Nova data nascimento (dd/mm/aaaa): ");
            String email = console.readInput("Novo email: ");
            String login = console.readInput("Novo login: ");
            String senha = console.readInput("Nova senha: ");
            switch (tipo) {
                case "1" -> System.out.println("Aluno atualizado: " + facade.atualizarAluno(id, nome, data, email, login, senha).getNome());
                case "2" -> System.out.println("Professor atualizado: " + facade.atualizarProfessor(id, nome, data, email, login, senha).getNome());
                case "3" -> System.out.println("Atendente atualizado: " + facade.atualizarAtendente(id, nome, data, email, login, senha).getNome());
                case "4" -> System.out.println("Administrador atualizado: " + facade.atualizarAdministrador(id, nome, data, email, login, senha).getNome());
                default  -> System.out.println("Tipo invalido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void menuDeletar() {
        console.clearConsole();
        console.printHeader("Deletar Usuario");
        System.out.println("1. Aluno  2. Professor  3. Atendente  4. Administrador");
        String tipo = console.readInput("Tipo: ");
        String idStr = console.readInput("ID do usuario: ");
        try {
            long id = Long.parseLong(idStr);
            switch (tipo) {
                case "1" -> { facade.deletarAluno(id); System.out.println("Aluno deletado."); }
                case "2" -> { facade.deletarProfessor(id); System.out.println("Professor deletado."); }
                case "3" -> { facade.deletarAtendente(id); System.out.println("Atendente deletado."); }
                case "4" -> { facade.deletarAdministrador(id); System.out.println("Administrador deletado."); }
                default  -> System.out.println("Tipo invalido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void menuTurmas() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Gerenciar Turmas");
            System.out.println("1. Criar Turma");
            System.out.println("2. Listar Turmas");
            System.out.println("3. Adicionar Aluno a Turma");
            System.out.println("4. Atualizar Turma");
            System.out.println("5. Deletar Turma");
            System.out.println("0. Voltar");
            String op = console.readInput("Opcao: ");
            switch (op) {
                case "1" -> criarTurma();
                case "2" -> listarTurmas();
                case "3" -> adicionarAlunoTurma();
                case "4" -> atualizarTurma();
                case "5" -> deletarTurma();
                case "0" -> running = false;
                default  -> System.out.println("Opcao invalida.");
            }
        }
    }

    private void criarTurma() {
        String nome = console.readInput("Nome da turma: ");
        String profIdStr = console.readInput("ID do professor: ");
        String horario = console.readInput("Horario: ");
        try {
            long profId = Long.parseLong(profIdStr);
            Turma turma = facade.cadastrarTurma(nome, profId, horario);
            System.out.println("Turma criada: " + turma.getNome() + " [ID:" + turma.getId() + "]");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void listarTurmas() {
        List<Turma> turmas = facade.listarTurmas();
        if (turmas.isEmpty()) System.out.println("Nenhuma turma cadastrada.");
        else turmas.forEach(t -> System.out.println(t.infos()));
        console.waitForEnter();
    }

    private void adicionarAlunoTurma() {
        try {
            long turmaId = Long.parseLong(console.readInput("ID da turma: "));
            long alunoId = Long.parseLong(console.readInput("ID do aluno: "));
            facade.adicionarAlunoATurma(turmaId, alunoId);
            System.out.println("Aluno adicionado a turma com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void atualizarTurma() {
        try {
            long id = Long.parseLong(console.readInput("ID da turma: "));
            String nome = console.readInput("Novo nome: ");
            long profId = Long.parseLong(console.readInput("ID do professor: "));
            String horario = console.readInput("Novo horario: ");
            Turma t = facade.atualizarTurma(id, nome, profId, horario);
            System.out.println("Turma atualizada: " + t.getNome());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void deletarTurma() {
        try {
            long id = Long.parseLong(console.readInput("ID da turma: "));
            facade.deletarTurma(id);
            System.out.println("Turma deletada.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void gerarRelatorio(String formato) {
        console.clearConsole();
        String relatorio;
        if ("HTML".equals(formato)) {
            relatorio = new HTMLRelatorio().gerarRelatorio(facade, loginHistory);
        } else {
            relatorio = new PDFRelatorio().gerarRelatorio(facade, loginHistory);
        }
        System.out.println(relatorio);
        console.waitForEnter();
    }

    private void mostrarEstatisticas() {
        console.clearConsole();
        console.printHeader("Estatisticas do Sistema");
        Map<String, Integer> contagens = facade.contarEntidadesPorTipo();
        contagens.forEach((tipo, qtd) ->
                System.out.println("  " + tipo + ": " + qtd));
        System.out.println("\nTotal: " + facade.contarEntidades() + " entidades");
        console.waitForEnter();
    }
}
