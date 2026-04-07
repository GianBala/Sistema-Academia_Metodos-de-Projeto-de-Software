package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.repository.LoginHistoryRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SqliteLoginHistoryRepository implements LoginHistoryRepository {

    private final Connection connection;

    public SqliteLoginHistoryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void registrar(String login, String tipoEntidade, LocalDateTime dataHora) {
        String sql = "INSERT INTO login_history (login, tipo_entidade, data_hora) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, tipoEntidade);
            stmt.setString(3, dataHora.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar login.", e);
        }
    }

    @Override
    public long contarLogins(String tipoEntidade) {
        String sql = "SELECT COUNT(*) FROM login_history WHERE tipo_entidade = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoEntidade);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar logins.", e);
        }
        return 0;
    }

    @Override
    public List<String> ultimosLogins(int limite) {
        String sql = "SELECT login, tipo_entidade, data_hora FROM login_history ORDER BY id DESC LIMIT ?";
        List<String> registros = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limite);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(String.format("[%s] %s (%s)",
                            rs.getString("data_hora"),
                            rs.getString("login"),
                            rs.getString("tipo_entidade")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar últimos logins.", e);
        }
        return registros;
    }

    @Override
    public Map<String, Long> loginsPorTipo() {
        String sql = "SELECT tipo_entidade, COUNT(*) as total FROM login_history GROUP BY tipo_entidade";
        Map<String, Long> mapa = new LinkedHashMap<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mapa.put(rs.getString("tipo_entidade"), rs.getLong("total"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar logins por tipo.", e);
        }
        return mapa;
    }
}
