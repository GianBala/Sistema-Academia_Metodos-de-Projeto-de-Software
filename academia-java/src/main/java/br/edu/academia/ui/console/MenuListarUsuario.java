package br.edu.academia.ui.console;

import br.edu.academia.application.FacadeSingletonController;
import br.edu.academia.domain.entity.Entidade;

import java.util.List;

public class MenuListarUsuario {

    private final FacadeSingletonController facade;
    private final ConsoleUtils console;

    public MenuListarUsuario(FacadeSingletonController facade, ConsoleUtils console) {
        this.facade = facade;
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
            case "1" -> printEntidades("Alunos", facade.listarAlunos());
            case "2" -> listarTodos();
            case "3" -> { return; }
            default -> System.out.println("Opcao invalida.");
        }
        console.waitForEnter();
    }

    private void listarTodos() {
        printEntidades("Administradores", facade.listarAdministradores());
        printEntidades("Alunos", facade.listarAlunos());
        printEntidades("Atendentes", facade.listarAtendentes());
        printEntidades("Professores", facade.listarProfessores());
        System.out.println("Total de entidades cadastradas: " + facade.contarTotalEntidades());
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
