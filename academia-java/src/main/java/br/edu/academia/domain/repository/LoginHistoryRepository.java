package br.edu.academia.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface LoginHistoryRepository {
    void registrar(String login, String tipoEntidade, LocalDateTime dataHora);
    long contarLogins(String tipoEntidade);
    List<String> ultimosLogins(int limite);
    Map<String, Long> loginsPorTipo();
}
