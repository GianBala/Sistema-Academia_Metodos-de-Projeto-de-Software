package br.edu.academia.ui.console;

import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.LoginHistoryRepository;
import br.edu.academia.domain.service.AuthResult;
import br.edu.academia.domain.service.AuthenticationService;

import java.util.Optional;

/**
 * Menu principal — ponto de entrada da aplicação.
 * Exibe tela de login e encaminha para o menu do papel correspondente.
 */
public class MenuPrincipal {

    private final MenuLogin menuLogin;
    private final AcademiaFacade facade;
    private final HistoricoOperacoes historico;
    private final LoginHistoryRepository loginHistory;
    private final ConsoleUtils console;

    public MenuPrincipal(AuthenticationService authService,
                          AcademiaFacade facade,
                          HistoricoOperacoes historico,
                          LoginHistoryRepository loginHistory,
                          ConsoleUtils console) {
        this.menuLogin = new MenuLogin(authService, console);
        this.facade = facade;
        this.historico = historico;
        this.loginHistory = loginHistory;
        this.console = console;
    }

    public void executar() {
        boolean running = true;
        while (running) {
            console.clearConsole();
            console.printHeader("Sistema Academia - Hagwon");
            System.out.println("1. Login");
            System.out.println("2. Sair");
            System.out.println();

            String escolha = console.readInput("Opcao: ");
            switch (escolha) {
                case "1" -> {
                    Optional<AuthResult> auth = menuLogin.executar();
                    auth.ifPresent(this::encaminharParaMenu);
                }
                case "2" -> {
                    System.out.println("Encerrando o sistema. Ate logo!");
                    running = false;
                }
                default -> {
                    System.out.println("Opcao invalida.");
                    console.waitForEnter();
                }
            }
        }
    }

    private void encaminharParaMenu(AuthResult auth) {
        switch (auth.getRole()) {
            case ADMINISTRADOR -> new MenuAdministrador(facade, historico, loginHistory, console).executar();
            case ATENDENTE     -> new MenuAtendente(facade, historico, console).executar();
            case PROFESSOR     -> new MenuProfessor(facade, console).executar();
            case ALUNO         -> new MenuAluno(facade, console).executar();
            default            -> System.out.println("Papel desconhecido: " + auth.getRole());
        }
    }
}
