package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.repository.AdministradorRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteAdministradorRepository implements AdministradorRepository {

    private final Connection connection;

    public SqliteAdministradorRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Administrador admin) {
        String sql = "INSERT INTO administradores (nome, dt_nascimento, email, login, senha_hash) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, admin.getNome());
            stmt.setString(2, admin.getDataNascimento().toString());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getLogin());
            stmt.setString(5, admin.getSenhaHash());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    admin.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar administrador.", e);
        }
    }

    @Override
    public List<Administrador> findAll() {
        String sql = "SELECT id, nome, dt_nascimento, email, login, senha_hash FROM administradores";
        List<Administrador> admins = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                admins.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar administradores.", e);
        }
        return admins;
    }

    @Override
    public Optional<Administrador> findById(long id) {
        String sql = "SELECT id, nome, dt_nascimento, email, login, senha_hash FROM administradores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar administrador por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Administrador> findByLogin(String login) {
        String sql = "SELECT id, nome, dt_nascimento, email, login, senha_hash FROM administradores WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar administrador por login.", e);
        }
        return Optional.empty();
    }

    private Administrador mapRow(ResultSet rs) throws SQLException {
        Administrador admin = new Administrador(
                rs.getString("nome"),
                LocalDate.parse(rs.getString("dt_nascimento")),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("senha_hash")
        );
        admin.setId(rs.getLong("id"));
        return admin;
    }
}
