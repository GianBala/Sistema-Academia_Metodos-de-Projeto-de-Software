package br.edu.academia.ui.console;

import br.edu.academia.application.FacadeSingletonController;

public class MenuCadastrarUsuario {

    private final FacadeSingletonController facade;
    private final ConsoleUtils console;

    public MenuCadastrarUsuario(FacadeSingletonController facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    public void executar() {
        console.clearConsole();
        console.printHeader("Cadastrar Usuario");
        System.out.println("1. Administrador");
        System.out.println("2. Aluno");
        System.out.println("3. Funcionario");
        System.out.println();

        String tipo = console.readInput("Escolha o tipo de usuario: ");

        String nome = console.readInput("Nome: ");
        String dataNascimento = console.readInput("Data de Nascimento (dd/mm/aaaa): ");
        String email = console.readInput("Email: ");

        try {
            switch (tipo) {
                case "1" -> cadastrarAdministrador(nome, dataNascimento, email);
                case "2" -> cadastrarAluno(nome, dataNascimento, email);
                case "3" -> cadastrarFuncionario(nome, dataNascimento, email);
                default -> System.out.println("Tipo de usuario invalido.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void cadastrarAdministrador(String nome, String dataNascimento, String email) {
        String login = console.readInput("Login: ");
        String senha = console.readInput("Senha: ");
        var admin = facade.cadastrarAdministrador(nome, dataNascimento, email, login, senha);
        System.out.println("Administrador " + admin.getNome() + " cadastrado com sucesso!");
    }

    private void cadastrarAluno(String nome, String dataNascimento, String email) {
        var aluno = facade.cadastrarAluno(nome, dataNascimento, email);
        System.out.println("Aluno " + aluno.getNome() + " cadastrado com sucesso! Matricula: " + aluno.getMatricula());
    }

    private void cadastrarFuncionario(String nome, String dataNascimento, String email) {
        System.out.println("1. Professor");
        System.out.println("2. Atendente");
        String subTipo = console.readInput("Escolha o tipo de funcionario: ");

        switch (subTipo) {
            case "1" -> {
                var professor = facade.cadastrarProfessor(nome, dataNascimento, email);
                System.out.println("Professor " + professor.getNome() + " cadastrado com sucesso!");
            }
            case "2" -> {
                var atendente = facade.cadastrarAtendente(nome, dataNascimento, email);
                System.out.println("Atendente " + atendente.getNome() + " cadastrado com sucesso!");
            }
            default -> System.out.println("Tipo de funcionario invalido.");
        }
    }
}
