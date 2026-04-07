package br.edu.academia.domain.logging;

/**
 * Interface de logging customizada — ponto de entrada para o padrão Adapter.
 * A camada de domínio depende apenas desta abstração, não de implementações concretas.
 */
public interface Logger {
    void info(String message);
    void warn(String message);
    void error(String message);
    void debug(String message);
}
