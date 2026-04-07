package br.edu.academia.ui.console;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.entity.Treino;
import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.service.SessionContext;

import java.util.List;

/**
 * Menu do Aluno — visualiza seus próprios dados e treinos.
 */
public class MenuAluno {

    private final AcademiaFacade facade;
    private final ConsoleUtils console;

    public MenuAluno(AcademiaFacade facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            Aluno aluno = (Aluno) SessionContext.getCurrentUser();
            console.printHeader("Menu Aluno - " + aluno.getNome());
            System.out.println("1. Ver meus dados");
            System.out.println("2. Ver meus treinos");
            System.out.println("0. Sair / Logout");
            System.out.println();

            String escolha = console.readInput("Opcao: ");
            switch (escolha) {
                case "1" -> { System.out.println(aluno.infos()); console.waitForEnter(); }
                case "2" -> verTreinos(aluno.getId());
                case "0" -> { SessionContext.clearSession(); running = false; }
                default  -> { System.out.println("Opcao invalida."); console.waitForEnter(); }
            }
        }
    }

    private void verTreinos(long alunoId) {
        List<Treino> treinos = facade.listarTreinosDoAluno(alunoId);
        if (treinos.isEmpty()) {
            System.out.println("Nenhum treino prescrito ainda.");
        } else {
            System.out.println("Seus treinos:");
            treinos.forEach(t -> System.out.println("  " + t.infos()));
        }
        console.waitForEnter();
    }
}
