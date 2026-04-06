package br.edu.academia.ui.console;

import br.edu.academia.ui.console.command.Command;

public class MenuListarUsuario {

    private final Command listarCommand;
    private final ConsoleUtils console;

    public MenuListarUsuario(Command listarCommand, ConsoleUtils console) {
        this.listarCommand = listarCommand;
        this.console = console;
    }

    public void executar() {
        console.clearConsole();
        console.printHeader("Listar Usuarios");
        listarCommand.execute();
        console.waitForEnter();
    }
}