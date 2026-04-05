package br.edu.academia.ui.console.command;

import br.edu.academia.domain.service.AdministradorService;
import br.edu.academia.ui.console.ConsoleUtils;

public class CadastrarAdministradorCommand implements Command {

    private final AdministradorService administradorService;
    private final ConsoleUtils console;

    public CadastrarAdministradorCommand(AdministradorService administradorService, ConsoleUtils console) {
        this.administradorService = administradorService;
        this.console = console;
    }

    @Override
    public void execute() {
        String nome = console.readInput("Nome: ");
        String dataNascimento = console.readInput("Data de Nascimento (dd/mm/aaaa): ");
        String email = console.readInput("Email: ");
        String login = console.readInput("Login: ");
        String senha = console.readInput("Senha: ");

        try {
            var admin = administradorService.cadastrar(nome, dataNascimento, email, login, senha);
            System.out.println("Administrador " + admin.getNome() + " cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }
}