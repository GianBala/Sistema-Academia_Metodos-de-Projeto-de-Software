package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Professor;
import br.edu.academia.domain.repository.ProfessorRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteProfessorRepository implements ProfessorRepository {

    private final Connection connection;

    public SqliteProfessorRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Professor professor) {
        String sql = "INSERT INTO professores (nome, dt_nascimento, email, login, senha_hash) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDataNascimento().toString());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getLogin());
            stmt.setString(5, professor.getSenhaHash());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) professor.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar professor.", e);
        }
    }

    @Override
    public List<Professor> findAll() {
        String sql = "SELECT id, nome, dt_nascimento, email, login, senha_hash FROM professores";
        List<Professor> professores = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) professores.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar professores.", e);
        }
        return professores;
    }

    @Override
    public Optional<Professor> findById(long id) {
        String sql = "SELECT id, nome, dt_nascimento, email, login, senha_hash FROM professores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar professor por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Professor> findByLogin(String login) {
        String sql = "SELECT id, nome, dt_nascimento, email, login, senha_hash FROM professores WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar professor por login.", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM professores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar professor.", e);
        }
    }

    @Override
    public void update(Professor professor) {
        String sql = "UPDATE professores SET nome = ?, dt_nascimento = ?, email = ?, login = ?, senha_hash = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDataNascimento().toString());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getLogin());
            stmt.setString(5, professor.getSenhaHash());
            stmt.setLong(6, professor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar professor.", e);
        }
    }

    private Professor mapRow(ResultSet rs) throws SQLException {
        Professor professor = new Professor.Builder()
                .nome(rs.getString("nome"))
                .dataNascimento(LocalDate.parse(rs.getString("dt_nascimento")))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .senhaHash(rs.getString("senha_hash"))
                .build();
        professor.setId(rs.getLong("id"));
        return professor;
    }
}
