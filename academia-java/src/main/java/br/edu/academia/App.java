package br.edu.academia;

import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.Repository;
import br.edu.academia.domain.service.*;
import br.edu.academia.infrastructure.database.*;
import br.edu.academia.infrastructure.security.BCryptPasswordHasher;
import br.edu.academia.infrastructure.security.PasswordHasher;
import br.edu.academia.ui.console.*;
import br.edu.academia.ui.console.command.*;

import java.util.Map;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        DatabaseConnection db = DatabaseConnection.getInstance();

        var connection = db.getConnection();
        var alunoRepo = new SqliteAlunoRepository(connection);
        var professorRepo = new SqliteProfessorRepository(connection);
        var atendenteRepo = new SqliteAtendenteRepository(connection);
        var adminRepo = new SqliteAdministradorRepository(connection);

        MatriculaGenerator matriculaGenerator = new RandomMatriculaGenerator(alunoRepo);
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        Map<TipoEntidade, Repository<?>> repositoriosPorTipo = Map.of(
                TipoEntidade.ALUNO, alunoRepo,
                TipoEntidade.PROFESSOR, professorRepo,
                TipoEntidade.ATENDENTE, atendenteRepo,
                TipoEntidade.ADMINISTRADOR, adminRepo
        );
        var historico = new HistoricoOperacoes(repositoriosPorTipo);

        var alunoService = new AlunoService(alunoRepo, matriculaGenerator, historico);
        var professorService = new ProfessorService(professorRepo, historico);
        var atendenteService = new AtendenteService(atendenteRepo, historico);
        var adminService = new AdministradorService(adminRepo, passwordHasher, historico);

        var scanner = new Scanner(System.in);
        var console = new ConsoleUtils(scanner);

        Map<String, Command> comandosCadastro = Map.of(
                "1", new CadastrarAdministradorCommand(adminService, console),
                "2", new CadastrarAlunoCommand(alunoService, console),
                "3", new CadastrarProfessorCommand(professorService, console),
                "4", new CadastrarAtendenteCommand(atendenteService, console)
        );

        Command listarCommand = new ListarUsuariosCommand(
                alunoService, professorService, atendenteService, adminService, console);
        
        var relatorio = new RelatorioService(
            "Relatório final", alunoRepo, professorRepo, atendenteRepo, adminRepo);
        
        var menuPrincipal = new MenuPrincipal(menuCadastrar, menuListar, console, relatorio);

        menuPrincipal.executar();

        scanner.close();
        db.close();
    }
}