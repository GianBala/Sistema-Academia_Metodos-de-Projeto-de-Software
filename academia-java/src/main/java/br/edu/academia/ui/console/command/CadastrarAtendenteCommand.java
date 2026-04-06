package br.edu.academia.ui.console.command;

import br.edu.academia.domain.service.AtendenteService;
import br.edu.academia.ui.console.ConsoleUtils;

public class CadastrarAtendenteCommand implements Command {

    private final AtendenteService atendenteService;
    private final ConsoleUtils console;

    public CadastrarAtendenteCommand(AtendenteService atendenteService, ConsoleUtils console) {
        this.atendenteService = atendenteService;
        this.console = console;
    }

    @Override
    public void execute() {
        String nome = console.readInput("Nome: ");
        String dataNascimento = console.readInput("Data de Nascimento (dd/mm/aaaa): ");
        String email = console.readInput("Email: ");

        try {
            var atendente = atendenteService.cadastrar(nome, dataNascimento, email);
            System.out.println("Atendente " + atendente.getNome() + " cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }
}