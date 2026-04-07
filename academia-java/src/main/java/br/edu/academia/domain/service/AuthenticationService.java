package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.memento.TipoEntidade;
import br.edu.academia.domain.repository.*;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Serviço de autenticação — verifica credenciais e inicializa sessão.
 * Registra logins bem-sucedidos no histórico de acessos.
 */
public class AuthenticationService {

    private final AlunoRepository alunoRepo;
    private final ProfessorRepository professorRepo;
    private final AtendenteRepository atendenteRepo;
    private final AdministradorRepository adminRepo;
    private final LoginHistoryRepository loginHistoryRepo;
    private final PasswordHasher passwordHasher;
    private final Logger logger;

    public AuthenticationService(AlunoRepository alunoRepo,
                                  ProfessorRepository professorRepo,
                                  AtendenteRepository atendenteRepo,
                                  AdministradorRepository adminRepo,
                                  LoginHistoryRepository loginHistoryRepo,
                                  PasswordHasher passwordHasher,
                                  Logger logger) {
        this.alunoRepo = alunoRepo;
        this.professorRepo = professorRepo;
        this.atendenteRepo = atendenteRepo;
        this.adminRepo = adminRepo;
        this.loginHistoryRepo = loginHistoryRepo;
        this.passwordHasher = passwordHasher;
        this.logger = logger;
    }

    /**
     * Tenta autenticar o usuário pesquisando em todos os repositórios.
     * @return AuthResult se autenticado, Optional.empty() se falhou
     */
    public Optional<AuthResult> autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            logger.warn("Tentativa de login com credenciais vazias.");
            return Optional.empty();
        }

        // Verifica administradores
        Optional<Administrador> admin = adminRepo.findByLogin(login);
        if (admin.isPresent() && passwordHasher.verify(senha, admin.get().getSenhaHash())) {
            registrarLogin(login, "ADMINISTRADOR");
            return Optional.of(new AuthResult(admin.get(), TipoEntidade.ADMINISTRADOR));
        }

        // Verifica atendentes
        Optional<Atendente> atendente = atendenteRepo.findByLogin(login);
        if (atendente.isPresent() && passwordHasher.verify(senha, atendente.get().getSenhaHash())) {
            registrarLogin(login, "ATENDENTE");
            return Optional.of(new AuthResult(atendente.get(), TipoEntidade.ATENDENTE));
        }

        // Verifica professores
        Optional<Professor> professor = professorRepo.findByLogin(login);
        if (professor.isPresent() && passwordHasher.verify(senha, professor.get().getSenhaHash())) {
            registrarLogin(login, "PROFESSOR");
            return Optional.of(new AuthResult(professor.get(), TipoEntidade.PROFESSOR));
        }

        // Verifica alunos
        Optional<Aluno> aluno = alunoRepo.findByLogin(login);
        if (aluno.isPresent() && passwordHasher.verify(senha, aluno.get().getSenhaHash())) {
            registrarLogin(login, "ALUNO");
            return Optional.of(new AuthResult(aluno.get(), TipoEntidade.ALUNO));
        }

        logger.warn("Falha de autenticacao para login: " + login);
        return Optional.empty();
    }

    private void registrarLogin(String login, String tipo) {
        loginHistoryRepo.registrar(login, tipo, LocalDateTime.now());
        logger.info("Login bem-sucedido: " + login + " (" + tipo + ")");
    }
}
