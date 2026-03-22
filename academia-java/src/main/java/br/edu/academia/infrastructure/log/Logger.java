package br.edu.academia.infrastructure.log;

public interface Logger {

    void info(String message);

    void warn(String message);

    void error(String message);
}
