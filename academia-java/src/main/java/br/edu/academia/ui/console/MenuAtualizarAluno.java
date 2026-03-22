package br.edu.academia.ui.console;

import br.edu.academia.application.FacadeSingletonController;
import br.edu.academia.domain.entity.Aluno;

import java.util.List;

public class MenuAtualizarAluno {

    private final FacadeSingletonController facade;
    private final ConsoleUtils console;

    public MenuAtualizarAluno(FacadeSingletonController facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    public void executar() {
        console.clearConsole();
        console.printHeader("Atualizar Aluno");

        List<Aluno> alunos = facade.listarAlunos();
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            console.waitForEnter();
            return;
        }

        System.out.println("Alunos cadastrados:");
        for (Aluno a : alunos) {
            System.out.println("  [" + a.getId() + "] " + a.infos());
        }
        System.out.println();

        String idStr = console.readInput("ID do aluno a atualizar: ");
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
            console.waitForEnter();
            return;
        }

        String nome = console.readInput("Novo nome: ");
        String dataNascimento = console.readInput("Nova data de nascimento (dd/MM/yyyy): ");
        String email = console.readInput("Novo email: ");

        try {
            Aluno atualizado = facade.atualizarAluno(id, nome, dataNascimento, email);
            System.out.println("Aluno atualizado com sucesso: " + atualizado.infos());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        console.waitForEnter();
    }

    public void executarDesfazer() {
        if (!facade.temAtualizacaoParaDesfazer()) {
            System.out.println("Nenhuma atualizacao para desfazer.");
            console.waitForEnter();
            return;
        }

        try {
            facade.desfazerUltimaAtualizacaoAluno();
            System.out.println("Ultima atualizacao desfeita com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao desfazer: " + e.getMessage());
        }

        console.waitForEnter();
    }
}
