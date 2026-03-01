package br.edu.academia;

import br.edu.academia.application.FacadeSingletonController;
import br.edu.academia.domain.service.*;
import br.edu.academia.infrastructure.database.*;
import br.edu.academia.infrastructure.security.BCryptPasswordHasher;
import br.edu.academia.infrastructure.security.PasswordHasher;
import br.edu.academia.ui.console.*;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        var connection = db.getConnection();
        var alunoRepo = new SqliteAlunoRepository(connection);
        var professorRepo = new SqliteProfessorRepository(connection);
        var atendenteRepo = new SqliteAtendenteRepository(connection);
        var adminRepo = new SqliteAdministradorRepository(connection);

        MatriculaGenerator matriculaGenerator = new RandomMatriculaGenerator(alunoRepo);
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        var alunoService = new AlunoService(alunoRepo, matriculaGenerator);
        var professorService = new ProfessorService(professorRepo);
        var atendenteService = new AtendenteService(atendenteRepo);
        var adminService = new AdministradorService(adminRepo, passwordHasher);

        var facade = FacadeSingletonController.getInstance(
                alunoService, professorService, atendenteService, adminService);

        var scanner = new Scanner(System.in);
        var console = new ConsoleUtils(scanner);

        var menuCadastrar = new MenuCadastrarUsuario(facade, console);
        var menuListar = new MenuListarUsuario(facade, console);
        var menuPrincipal = new MenuPrincipal(menuCadastrar, menuListar, console);

        menuPrincipal.executar();

        scanner.close();
        db.close();
    }
}
