package br.edu.academia.domain.facade.command;

/**
 * Comando parametrizado da camada de fachada — padrão Command com retorno de resultado.
 */
public interface FacadeCommand<T> {
    T execute();
}
