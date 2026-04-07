package br.edu.academia.ui.console;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.service.SessionContext;

import java.util.List;

/**
 * Menu do Professor — visualiza suas turmas, cria treinos para alunos.
 */
public class MenuProfessor {

    private final AcademiaFacade facade;
    private final ConsoleUtils console;

    public MenuProfessor(AcademiaFacade facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            Professor prof = (Professor) SessionContext.getCurrentUser();
            console.printHeader("Menu Professor - " + prof.getNome());
            System.out.println("1. Ver minhas turmas");
            System.out.println("2. Ver alunos de uma turma");
            System.out.println("3. Criar treino para aluno");
            System.out.println("4. Ver treinos de aluno");
            System.out.println("5. Atualizar treino");
            System.out.println("6. Deletar treino");
            System.out.println("0. Sair / Logout");
            System.out.println();

            String escolha = console.readInput("Opcao: ");
            switch (escolha) {
                case "1" -> verMinhasTurmas(prof.getId());
                case "2" -> verAlunosDaTurma();
                case "3" -> criarTreino();
                case "4" -> verTreinosAluno();
                case "5" -> atualizarTreino();
                case "6" -> deletarTreino();
                case "0" -> { SessionContext.clearSession(); running = false; }
                default  -> { System.out.println("Opcao invalida."); console.waitForEnter(); }
            }
        }
    }

    private void verMinhasTurmas(long professorId) {
        List<Turma> turmas = facade.listarTurmasDoProfessor(professorId);
        if (turmas.isEmpty()) {
            System.out.println("Voce nao possui turmas cadastradas.");
        } else {
            turmas.forEach(t -> System.out.println(t.infos()));
        }
        console.waitForEnter();
    }

    private void verAlunosDaTurma() {
        try {
            long turmaId = Long.parseLong(console.readInput("ID da turma: "));
            List<Long> alunoIds = facade.listarAlunosDaTurma(turmaId);
            if (alunoIds.isEmpty()) {
                System.out.println("Nenhum aluno nesta turma.");
            } else {
                System.out.println("Alunos na turma " + turmaId + ":");
                for (Long alunoId : alunoIds) {
                    facade.buscarAluno(alunoId).ifPresent(a ->
                            System.out.println("  [ID:" + a.getId() + "] " + a.getNome()
                                    + " | Matricula: " + a.getMatricula()));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void criarTreino() {
        try {
            long alunoId    = Long.parseLong(console.readInput("ID do aluno: "));
            String grupo    = console.readInput("Grupo muscular: ");
            String exercicio = console.readInput("Exercicio: ");
            int reps        = Integer.parseInt(console.readInput("Repeticoes: "));
            double peso     = Double.parseDouble(console.readInput("Peso (kg): "));
            Treino treino   = facade.cadastrarTreino(alunoId, grupo, exercicio, reps, peso);
            System.out.println("Treino criado: " + treino.infos());
        } catch (NumberFormatException e) {
            System.out.println("Valor numerico invalido.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void verTreinosAluno() {
        try {
            long alunoId = Long.parseLong(console.readInput("ID do aluno: "));
            List<Treino> treinos = facade.listarTreinosDoAluno(alunoId);
            if (treinos.isEmpty()) System.out.println("Nenhum treino cadastrado para este aluno.");
            else treinos.forEach(t -> System.out.println(t.infos()));
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void atualizarTreino() {
        try {
            long id         = Long.parseLong(console.readInput("ID do treino: "));
            String grupo    = console.readInput("Novo grupo muscular: ");
            String exercicio = console.readInput("Novo exercicio: ");
            int reps        = Integer.parseInt(console.readInput("Novas repeticoes: "));
            double peso     = Double.parseDouble(console.readInput("Novo peso (kg): "));
            Treino t = facade.atualizarTreino(id, grupo, exercicio, reps, peso);
            System.out.println("Treino atualizado: " + t.infos());
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }

    private void deletarTreino() {
        try {
            long id = Long.parseLong(console.readInput("ID do treino: "));
            facade.deletarTreino(id);
            System.out.println("Treino deletado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.waitForEnter();
    }
}
