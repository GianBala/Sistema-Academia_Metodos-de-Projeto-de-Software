package br.edu.academia.ui.console;

public class MenuPrincipal {

    private final MenuCadastrarUsuario menuCadastrar;
    private final MenuListarUsuario menuListar;
    private final MenuRelatorio menuRelatorio;
    private final ConsoleUtils console;

    public MenuPrincipal(MenuCadastrarUsuario menuCadastrar,
                         MenuListarUsuario menuListar,
                         MenuRelatorio menuRelatorio,
                         ConsoleUtils console) {
        this.menuCadastrar = menuCadastrar;
        this.menuListar = menuListar;
        this.menuRelatorio = menuRelatorio;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Sistema Academia");
            System.out.println("1. Cadastrar Usuario");
            System.out.println("2. Listar Usuarios");
            System.out.println("3. Gerar Relatorio");
            System.out.println("4. Sair");
            System.out.println();

            String escolha = console.readInput("Escolha uma opcao: ");

            switch (escolha) {
                case "1" -> menuCadastrar.executar();
                case "2" -> menuListar.executar();
                case "3" -> menuRelatorio.executar();
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
