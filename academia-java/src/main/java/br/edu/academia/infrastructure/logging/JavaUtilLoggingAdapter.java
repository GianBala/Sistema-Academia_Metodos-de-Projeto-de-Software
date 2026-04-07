package br.edu.academia.infrastructure.logging;

import br.edu.academia.domain.logging.Logger;

import java.util.logging.Level;

/**
 * Padrão Adapter — adapta a interface customizada {@link Logger} para a API
 * incompatível do {@code java.util.logging.Logger} (JUL).
 *
 * O domínio depende de {@code Logger} (interface de destino).
 * Esta classe é o Adaptador que conecta ao Adaptado (JUL).
 */
public class JavaUtilLoggingAdapter implements Logger {

    private final java.util.logging.Logger julLogger;

    public JavaUtilLoggingAdapter(String nome) {
        this.julLogger = java.util.logging.Logger.getLogger(nome);
    }

    @Override
    public void info(String message) {
        julLogger.log(Level.INFO, message);
    }

    @Override
    public void warn(String message) {
        julLogger.log(Level.WARNING, message);
    }

    @Override
    public void error(String message) {
        julLogger.log(Level.SEVERE, message);
    }

    @Override
    public void debug(String message) {
        julLogger.log(Level.FINE, message);
    }
}
