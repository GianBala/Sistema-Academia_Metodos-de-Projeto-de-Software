package br.edu.academia.ui.console.command;

import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.ui.console.ConsoleUtils;

public class CadastrarAlunoCommand implements Command {

    private final AcademiaFacade facade;
    private final ConsoleUtils console;

    public CadastrarAlunoCommand(AcademiaFacade facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    @Override
    public void execute() {
        console.printHeader("Cadastrar Aluno");
        String nome           = console.readInput("Nome: ");
        String dataNascimento = console.readInput("Data de Nascimento (dd/mm/aaaa): ");
        String email          = console.readInput("Email: ");
        String login          = console.readInput("Login (max 12 chars, sem numeros): ");
        String senha          = console.readInput("Senha (politica AWS IAM): ");

        try {
            var aluno = facade.cadastrarAluno(nome, dataNascimento, email, login, senha);
            System.out.println("Aluno " + aluno.getNome()
                    + " cadastrado com sucesso! Matricula: " + aluno.getMatricula());
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
