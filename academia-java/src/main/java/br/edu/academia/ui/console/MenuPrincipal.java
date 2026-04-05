package br.edu.academia.ui.console;

import br.edu.academia.ui.console.command.Command;

public class MenuPrincipal {

    private final MenuCadastrarUsuario menuCadastrar;
    private final MenuListarUsuario menuListar;
    private final Command desfazerCommand;
    private final ConsoleUtils console;

    public MenuPrincipal(MenuCadastrarUsuario menuCadastrar,
                         MenuListarUsuario menuListar,
                         Command desfazerCommand,
                         ConsoleUtils console) {
        this.menuCadastrar = menuCadastrar;
        this.menuListar = menuListar;
        this.desfazerCommand = desfazerCommand;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Sistema Academia");
            System.out.println("1. Cadastrar Usuario");
            System.out.println("2. Listar Usuarios");
            System.out.println("3. Desfazer ultimo cadastro");
            System.out.println("4. Sair");
            System.out.println();

            String escolha = console.readInput("Escolha uma opcao: ");

            switch (escolha) {
                case "1" -> menuCadastrar.executar();
                case "2" -> menuListar.executar();
                case "3" -> desfazerCommand.execute();
                case "4" -> {
                    System.out.println("Saindo do sistema...");
                    running = false;
                }
                default -> {
                    System.out.println("Opcao invalida. Tente novamente.");
                    console.waitForEnter();
                }
            }
        }
    }
}
