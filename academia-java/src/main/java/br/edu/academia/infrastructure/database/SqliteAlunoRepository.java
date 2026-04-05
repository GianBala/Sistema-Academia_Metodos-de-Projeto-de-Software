package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.repository.AlunoRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteAlunoRepository implements AlunoRepository {

    private final Connection connection;

    public SqliteAlunoRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Aluno aluno) {
        String sql = "INSERT INTO alunos (nome, dt_nascimento, email, matricula) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getDataNascimento().toString());
            stmt.setString(3, aluno.getEmail());
            stmt.setInt(4, aluno.getMatricula());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    aluno.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aluno.", e);
        }
    }

    @Override
    public List<Aluno> findAll() {
        String sql = "SELECT id, nome, dt_nascimento, email, matricula FROM alunos";
        List<Aluno> alunos = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alunos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos.", e);
        }
        return alunos;
    }

    @Override
    public Optional<Aluno> findById(long id) {
        String sql = "SELECT id, nome, dt_nascimento, email, matricula FROM alunos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Aluno> findByMatricula(int matricula) {
        String sql = "SELECT id, nome, dt_nascimento, email, matricula FROM alunos WHERE matricula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, matricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno por matricula.", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM alunos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar aluno.", e);
        }
    }

    private Aluno mapRow(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno.Builder()
            .nome(rs.getString("nome"))
            .dataNascimento(LocalDate.parse(rs.getString("dt_nascimento")))
            .email(rs.getString("email"))
            .matricula(rs.getInt("matricula"))
            .build();
        aluno.setId(rs.getLong("id"));
        return aluno;
    }
}
