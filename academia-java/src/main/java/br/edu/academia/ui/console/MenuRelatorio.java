package br.edu.academia.ui.console;

import br.edu.academia.application.FacadeSingletonController;

public class MenuRelatorio {

    private final FacadeSingletonController facade;
    private final ConsoleUtils console;

    public MenuRelatorio(FacadeSingletonController facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Gerar Relatorio");
            System.out.println("1. Gerar Relatorio HTML");
            System.out.println("2. Gerar Relatorio TXT");
            System.out.println("3. Voltar");
            System.out.println();

            String escolha = console.readInput("Escolha uma opcao: ");

            switch (escolha) {
                case "1" -> {
                    String caminho = facade.gerarRelatorioHtml();
                    System.out.println("Relatorio HTML gerado: " + caminho);
                    console.waitForEnter();
                }
                case "2" -> {
                    String caminho = facade.gerarRelatorioTxt();
                    System.out.println("Relatorio TXT gerado: " + caminho);
                    console.waitForEnter();
                }
                case "3" -> running = false;
                default -> {
                    System.out.println("Opcao invalida. Tente novamente.");
                    console.waitForEnter();
                }
            }
        }
    }
}
