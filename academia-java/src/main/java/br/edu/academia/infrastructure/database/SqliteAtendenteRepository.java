package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Atendente;
import br.edu.academia.domain.repository.AtendenteRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteAtendenteRepository implements AtendenteRepository {

    private final Connection connection;

    public SqliteAtendenteRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Atendente atendente) {
        String sql = "INSERT INTO atendentes (nome, dt_nascimento, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, atendente.getNome());
            stmt.setString(2, atendente.getDataNascimento().toString());
            stmt.setString(3, atendente.getEmail());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    atendente.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar atendente.", e);
        }
    }

    @Override
    public List<Atendente> findAll() {
        String sql = "SELECT id, nome, dt_nascimento, email FROM atendentes";
        List<Atendente> atendentes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                atendentes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar atendentes.", e);
        }
        return atendentes;
    }

    @Override
    public Optional<Atendente> findById(long id) {
        String sql = "SELECT id, nome, dt_nascimento, email FROM atendentes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar atendente por id.", e);
        }
        return Optional.empty();
    }

    private Atendente mapRow(ResultSet rs) throws SQLException {
        Atendente atendente = new Atendente(
                rs.getString("nome"),
                LocalDate.parse(rs.getString("dt_nascimento")),
                rs.getString("email")
        );
        atendente.setId(rs.getLong("id"));
        return atendente;
    }
}
