package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Turma;
import br.edu.academia.domain.repository.TurmaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteTurmaRepository implements TurmaRepository {

    private final Connection connection;

    public SqliteTurmaRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Turma turma) {
        String sql = "INSERT INTO turmas (nome, professor_id, horario) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, turma.getNome());
            stmt.setLong(2, turma.getProfessorId());
            stmt.setString(3, turma.getHorario());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) turma.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar turma.", e);
        }
    }

    @Override
    public List<Turma> findAll() {
        String sql = "SELECT id, nome, professor_id, horario FROM turmas";
        List<Turma> turmas = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) turmas.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar turmas.", e);
        }
        return turmas;
    }

    @Override
    public Optional<Turma> findById(long id) {
        String sql = "SELECT id, nome, professor_id, horario FROM turmas WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar turma por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Turma> findByProfessorId(long professorId) {
        String sql = "SELECT id, nome, professor_id, horario FROM turmas WHERE professor_id = ?";
        List<Turma> turmas = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, professorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) turmas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar turmas por professor.", e);
        }
        return turmas;
    }

    @Override
    public void addAluno(long turmaId, long alunoId) {
        String sql = "INSERT OR IGNORE INTO turma_alunos (turma_id, aluno_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, turmaId);
            stmt.setLong(2, alunoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar aluno à turma.", e);
        }
    }

    @Override
    public void removeAluno(long turmaId, long alunoId) {
        String sql = "DELETE FROM turma_alunos WHERE turma_id = ? AND aluno_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, turmaId);
            stmt.setLong(2, alunoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover aluno da turma.", e);
        }
    }

    @Override
    public List<Long> findAlunoIds(long turmaId) {
        String sql = "SELECT aluno_id FROM turma_alunos WHERE turma_id = ?";
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, turmaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ids.add(rs.getLong("aluno_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos da turma.", e);
        }
        return ids;
    }

    @Override
    public void delete(long id) {
        try {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM turma_alunos WHERE turma_id = ?")) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM turmas WHERE id = ?")) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar turma.", e);
        }
    }

    @Override
    public void update(Turma turma) {
        String sql = "UPDATE turmas SET nome = ?, professor_id = ?, horario = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, turma.getNome());
            stmt.setLong(2, turma.getProfessorId());
            stmt.setString(3, turma.getHorario());
            stmt.setLong(4, turma.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar turma.", e);
        }
    }

    private Turma mapRow(ResultSet rs) throws SQLException {
        Turma turma = new Turma.Builder()
                .nome(rs.getString("nome"))
                .professorId(rs.getLong("professor_id"))
                .horario(rs.getString("horario"))
                .build();
        turma.setId(rs.getLong("id"));
        return turma;
    }
}
