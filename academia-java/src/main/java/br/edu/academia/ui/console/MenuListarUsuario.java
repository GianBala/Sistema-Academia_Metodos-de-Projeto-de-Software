package br.edu.academia.ui.console;

import br.edu.academia.domain.entity.Entidade;
import br.edu.academia.domain.service.AdministradorService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.domain.service.AtendenteService;
import br.edu.academia.domain.service.ProfessorService;

import java.util.List;

public class MenuListarUsuario {

    private final AlunoService alunoService;
    private final ProfessorService professorService;
    private final AtendenteService atendenteService;
    private final AdministradorService administradorService;
    private final ConsoleUtils console;

    public MenuListarUsuario(AlunoService alunoService,
                             ProfessorService professorService,
                             AtendenteService atendenteService,
                             AdministradorService administradorService,
                             ConsoleUtils console) {
        this.alunoService = alunoService;
        this.professorService = professorService;
        this.atendenteService = atendenteService;
        this.administradorService = administradorService;
        this.console = console;
    }

    public void executar() {
        console.clearConsole();
        console.printHeader("Listar Usuarios");
        System.out.println("1. Listar Alunos");
        System.out.println("2. Listar Todos");
        System.out.println("3. Voltar");
        System.out.println();

        String escolha = console.readInput("Escolha uma opcao: ");

        switch (escolha) {
            case "1" -> printEntidades("Alunos", alunoService.listarTodos());
            case "2" -> listarTodos();
            case "3" -> { return; }
            default -> System.out.println("Opcao invalida.");
        }
        console.waitForEnter();
    }

    private void listarTodos() {
        printEntidades("Administradores", administradorService.listarTodos());
        printEntidades("Alunos", alunoService.listarTodos());
        printEntidades("Atendentes", atendenteService.listarTodos());
        printEntidades("Professores", professorService.listarTodos());
    }

    private void printEntidades(String titulo, List<? extends Entidade> entidades) {
        console.printSeparator("Lista de " + titulo);
        if (entidades.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
        } else {
            for (Entidade entidade : entidades) {
                System.out.println(entidade.infos());
            }
        }
        System.out.println();
    }
}
