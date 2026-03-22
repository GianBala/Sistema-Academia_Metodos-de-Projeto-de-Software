package br.edu.academia;

import br.edu.academia.application.FacadeSingletonController;
import br.edu.academia.domain.report.GeradorRelatorioHtml;
import br.edu.academia.domain.report.GeradorRelatorioTxt;
import br.edu.academia.domain.service.*;
import br.edu.academia.infrastructure.database.*;
import br.edu.academia.infrastructure.log.JavaLoggerAdapter;
import br.edu.academia.infrastructure.log.Logger;
import br.edu.academia.infrastructure.security.BCryptPasswordHasher;
import br.edu.academia.infrastructure.security.PasswordHasher;
import br.edu.academia.ui.console.*;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        var connection = db.getConnection();

        SqliteRepositoryFactory factory = new SqliteRepositoryFactory(connection);

        var alunoRepo = factory.createAlunoRepository();
        var professorRepo = factory.createProfessorRepository();
        var atendenteRepo = factory.createAtendenteRepository();
        var adminRepo = factory.createAdministradorRepository();
        var registroAcessoRepo = factory.createRegistroAcessoRepository();

        MatriculaGenerator matriculaGenerator = new RandomMatriculaGenerator(alunoRepo);
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        var alunoService = new AlunoService(alunoRepo, matriculaGenerator);
        var professorService = new ProfessorService(professorRepo);
        var atendenteService = new AtendenteService(atendenteRepo);
        var adminService = new AdministradorService(adminRepo, passwordHasher);
        var acessoService = new AcessoService(registroAcessoRepo);

        Logger logger = new JavaLoggerAdapter("br.edu.academia");

        var geradorHtml = new GeradorRelatorioHtml(acessoService);
        var geradorTxt = new GeradorRelatorioTxt(acessoService);

        var facade = FacadeSingletonController.getInstance(
                alunoService, alunoRepo, professorService, atendenteService, adminService,
                acessoService, logger, geradorHtml, geradorTxt);

        var scanner = new Scanner(System.in);
        var console = new ConsoleUtils(scanner);

        var menuCadastrar = new MenuCadastrarUsuario(facade, console);
        var menuListar = new MenuListarUsuario(facade, console);
        var menuRelatorio = new MenuRelatorio(facade, console);
        var menuAtualizar = new MenuAtualizarAluno(facade, console);
        var menuPrincipal = new MenuPrincipal(menuCadastrar, menuListar, menuRelatorio, menuAtualizar, console);

        menuPrincipal.executar();

        scanner.close();
        db.close();
    }
}
