package br.edu.academia.application;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.report.GeradorRelatorioHtml;
import br.edu.academia.domain.report.GeradorRelatorioTxt;
import br.edu.academia.domain.service.AcessoService;
import br.edu.academia.domain.service.AdministradorService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.domain.service.AtendenteService;
import br.edu.academia.domain.service.ProfessorService;
import br.edu.academia.infrastructure.log.Logger;

import java.util.List;

public class FacadeSingletonController {

    private static volatile FacadeSingletonController instance;

    private final AlunoService alunoService;
    private final ProfessorService professorService;
    private final AtendenteService atendenteService;
    private final AdministradorService administradorService;
    private final AcessoService acessoService;
    private final Logger logger;
    private final GeradorRelatorioHtml geradorHtml;
    private final GeradorRelatorioTxt geradorTxt;

    private FacadeSingletonController(
            AlunoService alunoService,
            ProfessorService professorService,
            AtendenteService atendenteService,
            AdministradorService administradorService,
            AcessoService acessoService,
            Logger logger,
            GeradorRelatorioHtml geradorHtml,
            GeradorRelatorioTxt geradorTxt) {
        this.alunoService = alunoService;
        this.professorService = professorService;
        this.atendenteService = atendenteService;
        this.administradorService = administradorService;
        this.acessoService = acessoService;
        this.logger = logger;
        this.geradorHtml = geradorHtml;
        this.geradorTxt = geradorTxt;
    }

    public static FacadeSingletonController getInstance(
            AlunoService alunoService,
            ProfessorService professorService,
            AtendenteService atendenteService,
            AdministradorService administradorService,
            AcessoService acessoService,
            Logger logger,
            GeradorRelatorioHtml geradorHtml,
            GeradorRelatorioTxt geradorTxt) {
        if (instance == null) {
            synchronized (FacadeSingletonController.class) {
                if (instance == null) {
                    instance = new FacadeSingletonController(
                            alunoService, professorService, atendenteService, administradorService,
                            acessoService, logger, geradorHtml, geradorTxt);
                }
            }
        }
        return instance;
    }

    static void resetForTesting() {
        instance = null;
    }

    public Aluno cadastrarAluno(String nome, String dataNascimento, String email) {
        Aluno aluno = alunoService.cadastrar(nome, dataNascimento, email);
        logger.info("Aluno cadastrado: " + nome);
        acessoService.registrarOperacao("CADASTRO", "ALUNO");
        return aluno;
    }

    public List<Aluno> listarAlunos() {
        logger.info("Listando alunos");
        acessoService.registrarOperacao("LISTAGEM", "ALUNO");
        return alunoService.listarTodos();
    }

    public Professor cadastrarProfessor(String nome, String dataNascimento, String email) {
        Professor professor = professorService.cadastrar(nome, dataNascimento, email);
        logger.info("Professor cadastrado: " + nome);
        acessoService.registrarOperacao("CADASTRO", "PROFESSOR");
        return professor;
    }

    public List<Professor> listarProfessores() {
        logger.info("Listando professores");
        acessoService.registrarOperacao("LISTAGEM", "PROFESSOR");
        return professorService.listarTodos();
    }

    public Atendente cadastrarAtendente(String nome, String dataNascimento, String email) {
        Atendente atendente = atendenteService.cadastrar(nome, dataNascimento, email);
        logger.info("Atendente cadastrado: " + nome);
        acessoService.registrarOperacao("CADASTRO", "ATENDENTE");
        return atendente;
    }

    public List<Atendente> listarAtendentes() {
        logger.info("Listando atendentes");
        acessoService.registrarOperacao("LISTAGEM", "ATENDENTE");
        return atendenteService.listarTodos();
    }

    public Administrador cadastrarAdministrador(String nome, String dataNascimento, String email,
                                                String login, String senha) {
        Administrador admin = administradorService.cadastrar(nome, dataNascimento, email, login, senha);
        logger.info("Administrador cadastrado: " + nome);
        acessoService.registrarOperacao("CADASTRO", "ADMINISTRADOR");
        return admin;
    }

    public List<Administrador> listarAdministradores() {
        logger.info("Listando administradores");
        acessoService.registrarOperacao("LISTAGEM", "ADMINISTRADOR");
        return administradorService.listarTodos();
    }

    public int contarTotalEntidades() {
        return alunoService.listarTodos().size()
                + professorService.listarTodos().size()
                + atendenteService.listarTodos().size()
                + administradorService.listarTodos().size();
    }

    public String gerarRelatorioHtml() {
        logger.info("Gerando relatorio HTML");
        return geradorHtml.gerarRelatorio();
    }

    public String gerarRelatorioTxt() {
        logger.info("Gerando relatorio TXT");
        return geradorTxt.gerarRelatorio();
    }
}
