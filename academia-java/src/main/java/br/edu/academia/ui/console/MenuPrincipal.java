package br.edu.academia.ui.console;

import br.edu.academia.domain.service.RelatorioService;

public class MenuPrincipal {

    private final MenuCadastrarUsuario menuCadastrar;
    private final MenuListarUsuario menuListar;
    private final Command desfazerCommand;
    private final ConsoleUtils console;
    private final RelatorioService retorio;

    public MenuPrincipal(MenuCadastrarUsuario menuCadastrar,
                         MenuListarUsuario menuListar,
                         ConsoleUtils console,
                         RelatorioService retorio) {
        this.menuCadastrar = menuCadastrar;
        this.menuListar = menuListar;
        this.desfazerCommand = desfazerCommand;
        this.console = console;
        this.retorio = retorio;
    }

    public void executar() {
        boolean running = true;
        String relatorio_str = new String();


        while (running) {
            console.clearConsole();
            console.printHeader("Sistema Academia - Hagwon");
            System.out.println("1. Cadastrar Usuario");
            System.out.println("2. Listar Usuarios");
            System.out.println("3. Gerar Relatório");
            System.out.println("4. Sair");
            System.out.println();

            String escolha = console.readInput("Escolha uma opcao: ");

            switch (escolha) {
                case "1" -> menuCadastrar.executar();
                case "2" -> menuListar.executar();
                case "3" -> {
                    relatorio_str = retorio.gerarRelatorio();
                    System.out.println(relatorio_str);
                    console.waitForEnter();
                }
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
