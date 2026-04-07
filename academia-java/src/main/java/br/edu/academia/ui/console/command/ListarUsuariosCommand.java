package br.edu.academia.ui.console.command;

import br.edu.academia.domain.entity.Entidade;
import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.ui.console.ConsoleUtils;

import java.util.List;

public class ListarUsuariosCommand implements Command {

    private final AcademiaFacade facade;
    private final ConsoleUtils console;

    public ListarUsuariosCommand(AcademiaFacade facade, ConsoleUtils console) {
        this.facade = facade;
        this.console = console;
    }

    @Override
    public void execute() {
        printEntidades("Administradores", facade.listarAdministradores());
        printEntidades("Professores", facade.listarProfessores());
        printEntidades("Atendentes", facade.listarAtendentes());
        printEntidades("Alunos", facade.listarAlunos());

        System.out.println();
        System.out.println("Total de entidades: " + facade.contarEntidades());
    }

    private void printEntidades(String titulo, List<? extends Entidade> entidades) {
        console.printSeparator("Lista de " + titulo);
        if (entidades.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
        } else {
            entidades.forEach(e -> System.out.println(e.infos()));
        }
        System.out.println();
    }
}
