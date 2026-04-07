package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Entidade;
import br.edu.academia.domain.memento.TipoEntidade;

/**
 * Contexto de sessão do usuário autenticado.
 * Usado pelo RepositoryProxy para verificar permissões.
 */
public class SessionContext {

    private static Entidade currentUser;
    private static TipoEntidade currentRole;

    public static void setSession(Entidade user, TipoEntidade role) {
        currentUser = user;
        currentRole = role;
    }

    public static void clearSession() {
        currentUser = null;
        currentRole = null;
    }

    public static Entidade getCurrentUser() { return currentUser; }
    public static TipoEntidade getCurrentRole() { return currentRole; }
    public static boolean isAuthenticated() { return currentUser != null; }
}
