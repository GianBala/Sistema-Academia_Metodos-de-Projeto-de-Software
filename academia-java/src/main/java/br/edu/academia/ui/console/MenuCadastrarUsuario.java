package br.edu.academia.ui.console;

import br.edu.academia.ui.console.command.Command;

import java.util.Map;

/**
 * Menu de cadastro — exibe opções e delega ao Command correspondente.
 * Mantido para compatibilidade com a estrutura de Commands.
 */
public class MenuCadastrarUsuario {

    private final Map<String, Command> comandos;
    private final ConsoleUtils console;

    public MenuCadastrarUsuario(Map<String, Command> comandos, ConsoleUtils console) {
        this.comandos = comandos;
        this.console = console;
    }

    public void executar() {
        console.clearConsole();
        console.printHeader("Cadastrar Usuario");
        System.out.println("1. Administrador");
        System.out.println("2. Aluno");
        System.out.println("3. Professor");
        System.out.println("4. Atendente");
        System.out.println();

        String escolha = console.readInput("Escolha o tipo de usuario: ");
        Command comando = comandos.get(escolha);

        if (comando != null) {
            comando.execute();
        } else {
            System.out.println("Tipo de usuario invalido.");
        }

        console.waitForEnter();
    }
}
