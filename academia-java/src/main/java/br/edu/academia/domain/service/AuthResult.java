package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Entidade;
import br.edu.academia.domain.memento.TipoEntidade;

/**
 * Resultado de uma tentativa de autenticação bem-sucedida.
 */
public class AuthResult {

    private final Entidade usuario;
    private final TipoEntidade role;

    public AuthResult(Entidade usuario, TipoEntidade role) {
        this.usuario = usuario;
        this.role = role;
    }

    public Entidade getUsuario() { return usuario; }
    public TipoEntidade getRole() { return role; }
}
