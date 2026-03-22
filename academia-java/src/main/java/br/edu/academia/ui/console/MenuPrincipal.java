package br.edu.academia.ui.console;

public class MenuPrincipal {

    private final MenuCadastrarUsuario menuCadastrar;
    private final MenuListarUsuario menuListar;
    private final MenuRelatorio menuRelatorio;
    private final MenuAtualizarAluno menuAtualizar;
    private final ConsoleUtils console;

    public MenuPrincipal(MenuCadastrarUsuario menuCadastrar,
                         MenuListarUsuario menuListar,
                         MenuRelatorio menuRelatorio,
                         MenuAtualizarAluno menuAtualizar,
                         ConsoleUtils console) {
        this.menuCadastrar = menuCadastrar;
        this.menuListar = menuListar;
        this.menuRelatorio = menuRelatorio;
        this.menuAtualizar = menuAtualizar;
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
            System.out.println("4. Atualizar Aluno");
            System.out.println("5. Desfazer Ultima Atualizacao de Aluno");
            System.out.println("6. Sair");
            System.out.println();

            String escolha = console.readInput("Escolha uma opcao: ");

            switch (escolha) {
                case "1" -> menuCadastrar.executar();
                case "2" -> menuListar.executar();
                case "3" -> menuRelatorio.executar();
                case "4" -> menuAtualizar.executar();
                case "5" -> menuAtualizar.executarDesfazer();
                case "6" -> {
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
