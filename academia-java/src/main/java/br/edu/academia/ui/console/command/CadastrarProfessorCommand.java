package br.edu.academia.ui.console.command;

import br.edu.academia.domain.service.ProfessorService;
import br.edu.academia.ui.console.ConsoleUtils;

public class CadastrarProfessorCommand implements Command{
    
    private final ProfessorService professorService;
    private final ConsoleUtils console;

    public CadastrarProfessorCommand(ProfessorService professorService, ConsoleUtils console) {
        this.professorService = professorService;
        this.console = console;
    }

    public void execute() {
        String nome = console.readInput("Nome: ");
        String dataNascimento = console.readInput("Data de Nascimento: ");
        String email = console.readInput("Email: ");

        try {
            var professor = professorService.cadastrar(nome, dataNascimento, email);
            System.out.println("Professor " + professor.getNome() + " cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }
}
