package br.edu.academia.application;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.service.AdministradorService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.domain.service.AtendenteService;
import br.edu.academia.domain.service.ProfessorService;

import java.util.List;

public class FacadeSingletonController {

    private static volatile FacadeSingletonController instance;

    private final AlunoService alunoService;
    private final ProfessorService professorService;
    private final AtendenteService atendenteService;
    private final AdministradorService administradorService;

    private FacadeSingletonController(
            AlunoService alunoService,
            ProfessorService professorService,
            AtendenteService atendenteService,
            AdministradorService administradorService) {
        this.alunoService = alunoService;
        this.professorService = professorService;
        this.atendenteService = atendenteService;
        this.administradorService = administradorService;
    }

    public static FacadeSingletonController getInstance(
            AlunoService alunoService,
            ProfessorService professorService,
            AtendenteService atendenteService,
            AdministradorService administradorService) {
        if (instance == null) {
            synchronized (FacadeSingletonController.class) {
                if (instance == null) {
                    instance = new FacadeSingletonController(
                            alunoService, professorService, atendenteService, administradorService);
                }
            }
        }
        return instance;
    }

    public Aluno cadastrarAluno(String nome, String dataNascimento, String email) {
        return alunoService.cadastrar(nome, dataNascimento, email);
    }

    public List<Aluno> listarAlunos() {
        return alunoService.listarTodos();
    }

    public Professor cadastrarProfessor(String nome, String dataNascimento, String email) {
        return professorService.cadastrar(nome, dataNascimento, email);
    }

    public List<Professor> listarProfessores() {
        return professorService.listarTodos();
    }

    public Atendente cadastrarAtendente(String nome, String dataNascimento, String email) {
        return atendenteService.cadastrar(nome, dataNascimento, email);
    }

    public List<Atendente> listarAtendentes() {
        return atendenteService.listarTodos();
    }

    public Administrador cadastrarAdministrador(String nome, String dataNascimento, String email,
                                                String login, String senha) {
        return administradorService.cadastrar(nome, dataNascimento, email, login, senha);
    }

    public List<Administrador> listarAdministradores() {
        return administradorService.listarTodos();
    }

    public int contarTotalEntidades() {
        return alunoService.listarTodos().size()
                + professorService.listarTodos().size()
                + atendenteService.listarTodos().size()
                + administradorService.listarTodos().size();
    }
}
