package br.edu.academia.ui.console.command;

import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.ui.console.ConsoleUtils;

public class CadastrarAlunoCommand implements Command {
    
    private final AlunoService alunoService;
    private final ConsoleUtils console;

    public CadastrarAlunoCommand(AlunoService alunoService, ConsoleUtils console) {
        this.alunoService = alunoService;
        this.console = console;
    }

    @Override
    public void execute() {
        String nome = console.readInput("Nome: ");
        String dataNascimento = console.readInput("Data de Nascimento (dd/mm/aaaa): ");
        String email = console.readInput("email");

        try {
            var aluno = alunoService.cadastrar(nome, dataNascimento, email);
            System.out.println("Aluno " + aluno.getNome() + " cadastrado com sucesso! Matricula: " + aluno.getMatricula());        
        } catch(IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

}
