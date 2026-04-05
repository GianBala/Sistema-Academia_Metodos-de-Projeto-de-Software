package br.edu.academia.ui.console.command;

import br.edu.academia.domain.entity.Entidade;
import br.edu.academia.domain.service.*;
import br.edu.academia.ui.console.ConsoleUtils;

import java.util.List;

public class ListarUsuariosCommand implements Command {

    private final AlunoService alunoService;
    private final ProfessorService professorService;
    private final AtendenteService atendenteService;
    private final AdministradorService administradorService;
    private final ConsoleUtils console;

    public ListarUsuariosCommand(AlunoService alunoService,
                                 ProfessorService professorService,
                                 AtendenteService atendenteService,
                                 AdministradorService administradorService,
                                 ConsoleUtils console) {
        this.alunoService = alunoService;
        this.professorService = professorService;
        this.atendenteService = atendenteService;
        this.administradorService = administradorService;
        this.console = console;
    }

    @Override
    public void execute() {
        printEntidades("Administradores", administradorService.listarTodos());
        printEntidades("Alunos", alunoService.listarTodos());
        printEntidades("Atendentes", atendenteService.listarTodos());
        printEntidades("Professores", professorService.listarTodos());
    }

    private void printEntidades(String titulo, List<? extends Entidade> entidades) {
        console.printSeparator("Lista de " + titulo);
        if (entidades.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
        } else {
            for (Entidade entidade : entidades) {
                System.out.println(entidade.infos());
            }
        }
        System.out.println();
    }
}