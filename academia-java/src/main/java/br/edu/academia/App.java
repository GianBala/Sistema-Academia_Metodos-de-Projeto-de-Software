package br.edu.academia;

import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.repository.*;
import br.edu.academia.domain.service.*;
import br.edu.academia.infrastructure.database.DatabaseConnection;
import br.edu.academia.infrastructure.database.SqliteRepositoryFactory;
import br.edu.academia.infrastructure.logging.JavaUtilLoggingAdapter;
import br.edu.academia.infrastructure.proxy.*;
import br.edu.academia.infrastructure.security.BCryptPasswordHasher;
import br.edu.academia.infrastructure.security.PasswordHasher;
import br.edu.academia.ui.console.ConsoleUtils;
import br.edu.academia.ui.console.MenuPrincipal;

import java.time.LocalDate;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // ── 1. Singleton: conexão ao banco ────────────────────────────────────
        DatabaseConnection db = DatabaseConnection.getInstance();

        // ── 2. Factory Method: criação dos repositórios ────────────────────────
        RepositoryFactory factory = new SqliteRepositoryFactory(db.getConnection());
        AlunoRepository alunoRepo          = factory.createAlunoRepository();
        ProfessorRepository professorRepo  = factory.createProfessorRepository();
        AtendenteRepository atendenteRepo  = factory.createAtendenteRepository();
        AdministradorRepository adminRepo  = factory.createAdministradorRepository();
        TurmaRepository turmaRepo          = factory.createTurmaRepository();
        TreinoRepository treinoRepo        = factory.createTreinoRepository();
        LoginHistoryRepository loginHistoryRepo = factory.createLoginHistoryRepository();

        // ── 3. Adapter: Logger que adapta java.util.logging para nossa interface
        Logger logger = new JavaUtilLoggingAdapter("AcademiaApp");

        // ── 4. Proxy: controle de acesso sobre repositórios ─────────────────────
        AlunoRepository alunoProxy          = new AlunoRepositoryProxy(alunoRepo);
        ProfessorRepository professorProxy  = new ProfessorRepositoryProxy(professorRepo);
        AtendenteRepository atendenteProxy  = new AtendenteRepositoryProxy(atendenteRepo);
        AdministradorRepository adminProxy  = new AdministradorRepositoryProxy(adminRepo);

        // ── 5. Memento: histórico de operações ────────────────────────────────
        HistoricoOperacoes historico = new HistoricoOperacoes();

        // ── 6. Serviços com dependências injetadas ────────────────────────────
        PasswordHasher passwordHasher = new BCryptPasswordHasher();

        MatriculaGenerator matriculaGenerator = new RandomMatriculaGenerator(alunoProxy);

        AlunoService alunoService         = new AlunoService(alunoProxy, matriculaGenerator, passwordHasher, historico, logger);
        ProfessorService professorService = new ProfessorService(professorProxy, passwordHasher, historico, logger);
        AtendenteService atendenteService = new AtendenteService(atendenteProxy, passwordHasher, historico, logger);
        AdministradorService adminService = new AdministradorService(adminProxy, passwordHasher, historico, logger);
        TurmaService turmaService         = new TurmaService(turmaRepo, historico, logger);
        TreinoService treinoService       = new TreinoService(treinoRepo, historico, logger);

        // ── 7. Facade + Singleton: ponto de acesso centralizado ───────────────
        AcademiaFacade.init(alunoService, professorService, atendenteService,
                            adminService, turmaService, treinoService);
        AcademiaFacade facade = AcademiaFacade.getInstance();

        // ── 8. Serviço de autenticação ────────────────────────────────────────
        AuthenticationService authService = new AuthenticationService(
                alunoRepo, professorRepo, atendenteRepo, adminRepo,
                loginHistoryRepo, passwordHasher, logger);

        // ── 9. Seed: cria admin padrão se não existir nenhum ─────────────────
        criarAdminPadraoSeNecessario(adminRepo, adminService, logger);

        // ── 10. Interface do usuário ──────────────────────────────────────────
        var scanner = new Scanner(System.in);
        var console = new ConsoleUtils(scanner);

        MenuPrincipal menuPrincipal = new MenuPrincipal(
                authService, facade, historico, loginHistoryRepo, console);
        menuPrincipal.executar();

        scanner.close();
        db.close();
    }

    /**
     * Se não houver nenhum administrador cadastrado, cria um admin padrão.
     * Login: admin | Senha: Admin@123
     */
    private static void criarAdminPadraoSeNecessario(AdministradorRepository adminRepo,
                                                      AdministradorService adminService,
                                                      Logger logger) {
        if (adminRepo.findAll().isEmpty()) {
            try {
                adminService.cadastrar(
                        "Administrador Padrao",
                        LocalDate.of(1990, 1, 1).toString(),
                        "admin@academia.com",
                        "admin",
                        "Admin@123"
                );
                logger.info("Admin padrao criado. Login: admin | Senha: Admin@123");
                System.out.println(">>> Admin padrao criado. Login: admin | Senha: Admin@123 <<<");
            } catch (Exception e) {
                logger.error("Erro ao criar admin padrao: " + e.getMessage());
            }
        }
    }
}
