package br.edu.academia.infrastructure.log;

public class JavaLoggerAdapter implements Logger {

    private final java.util.logging.Logger logger;

    public JavaLoggerAdapter(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }
}
