package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.RegistroAcesso;
import br.edu.academia.domain.repository.RegistroAcessoRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteRegistroAcessoRepository implements RegistroAcessoRepository {

    private final Connection connection;

    public SqliteRegistroAcessoRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(RegistroAcesso registro) {
        String sql = "INSERT INTO registro_acesso (tipo_operacao, tipo_entidade, timestamp) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, registro.getTipoOperacao());
            stmt.setString(2, registro.getTipoEntidade());
            stmt.setString(3, registro.getTimestamp().toString());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    registro.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar registro de acesso.", e);
        }
    }

    @Override
    public List<RegistroAcesso> findAll() {
        String sql = "SELECT id, tipo_operacao, tipo_entidade, timestamp FROM registro_acesso";
        List<RegistroAcesso> registros = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                registros.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar registros de acesso.", e);
        }
        return registros;
    }

    @Override
    public Optional<RegistroAcesso> findById(long id) {
        String sql = "SELECT id, tipo_operacao, tipo_entidade, timestamp FROM registro_acesso WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar registro de acesso por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<RegistroAcesso> findByTipoOperacao(String tipoOperacao) {
        String sql = "SELECT id, tipo_operacao, tipo_entidade, timestamp FROM registro_acesso WHERE tipo_operacao = ?";
        List<RegistroAcesso> registros = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoOperacao);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar registros por tipo de operacao.", e);
        }
        return registros;
    }

    @Override
    public List<RegistroAcesso> findByTipoEntidade(String tipoEntidade) {
        String sql = "SELECT id, tipo_operacao, tipo_entidade, timestamp FROM registro_acesso WHERE tipo_entidade = ?";
        List<RegistroAcesso> registros = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoEntidade);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    registros.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar registros por tipo de entidade.", e);
        }
        return registros;
    }

    @Override
    public long countByTipoOperacaoAndTipoEntidade(String tipoOperacao, String tipoEntidade) {
        String sql = "SELECT COUNT(*) FROM registro_acesso WHERE tipo_operacao = ? AND tipo_entidade = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoOperacao);
            stmt.setString(2, tipoEntidade);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar registros de acesso.", e);
        }
        return 0;
    }

    private RegistroAcesso mapRow(ResultSet rs) throws SQLException {
        RegistroAcesso registro = new RegistroAcesso(
                rs.getString("tipo_operacao"),
                rs.getString("tipo_entidade"),
                LocalDateTime.parse(rs.getString("timestamp"))
        );
        registro.setId(rs.getLong("id"));
        return registro;
    }
}
