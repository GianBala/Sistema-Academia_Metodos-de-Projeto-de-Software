package br.edu.academia.ui.console;

import br.edu.academia.domain.service.AuthResult;
import br.edu.academia.domain.service.AuthenticationService;
import br.edu.academia.domain.service.SessionContext;

import java.util.Optional;

/**
 * Menu de login — ponto de entrada para autenticação.
 * Após autenticação bem-sucedida, configura o SessionContext e retorna o resultado.
 */
public class MenuLogin {

    private final AuthenticationService authService;
    private final ConsoleUtils console;

    public MenuLogin(AuthenticationService authService, ConsoleUtils console) {
        this.authService = authService;
        this.console = console;
    }

    /**
     * @return AuthResult se login bem-sucedido, Optional.empty() se cancelado
     */
    public Optional<AuthResult> executar() {
        console.clearConsole();
        console.printHeader("Sistema Academia - Login");

        int tentativas = 0;
        final int MAX_TENTATIVAS = 3;

        while (tentativas < MAX_TENTATIVAS) {
            String login = console.readInput("Login: ");
            String senha = console.readInput("Senha: ");

            Optional<AuthResult> resultado = authService.autenticar(login, senha);
            if (resultado.isPresent()) {
                AuthResult auth = resultado.get();
                SessionContext.setSession(auth.getUsuario(), auth.getRole());
                System.out.println("\nBem-vindo(a), " + auth.getUsuario().getNome()
                        + "! [" + auth.getRole() + "]");
                console.waitForEnter();
                return resultado;
            }

            tentativas++;
            System.out.println("Login ou senha incorretos. Tentativa " + tentativas + "/" + MAX_TENTATIVAS);
        }

        System.out.println("Numero maximo de tentativas atingido.");
        return Optional.empty();
    }
}
