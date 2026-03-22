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
        String sql = "INSERT INTO professores (nome, dt_nascimento, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, professor.getNome());
            stmt.setString(2, professor.getDataNascimento().toString());
            stmt.setString(3, professor.getEmail());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    professor.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar professor.", e);
        }
    }

    @Override
    public List<Professor> findAll() {
        String sql = "SELECT id, nome, dt_nascimento, email FROM professores";
        List<Professor> professores = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                professores.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar professores.", e);
        }
        return professores;
    }

    @Override
    public Optional<Professor> findById(long id) {
        String sql = "SELECT id, nome, dt_nascimento, email FROM professores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar professor por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Professor entity) {
        throw new UnsupportedOperationException("Update nao implementado para Professor.");
    }

    private Professor mapRow(ResultSet rs) throws SQLException {
        Professor professor = new Professor(
                rs.getString("nome"),
                LocalDate.parse(rs.getString("dt_nascimento")),
                rs.getString("email")
        );
        professor.setId(rs.getLong("id"));
        return professor;
    }
}
