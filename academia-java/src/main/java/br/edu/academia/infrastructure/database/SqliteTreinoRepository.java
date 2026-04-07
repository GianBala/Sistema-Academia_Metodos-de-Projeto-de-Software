package br.edu.academia.infrastructure.database;

import br.edu.academia.domain.entity.Treino;
import br.edu.academia.domain.repository.TreinoRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteTreinoRepository implements TreinoRepository {

    private final Connection connection;

    public SqliteTreinoRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Treino treino) {
        String sql = "INSERT INTO treinos (aluno_id, grupo_muscular, exercicio, repeticoes, peso, data) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, treino.getAlunoId());
            stmt.setString(2, treino.getGrupoMuscular());
            stmt.setString(3, treino.getExercicio());
            stmt.setInt(4, treino.getRepeticoes());
            stmt.setDouble(5, treino.getPeso());
            stmt.setString(6, treino.getData().toString());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) treino.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar treino.", e);
        }
    }

    @Override
    public List<Treino> findAll() {
        String sql = "SELECT id, aluno_id, grupo_muscular, exercicio, repeticoes, peso, data FROM treinos";
        List<Treino> treinos = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) treinos.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar treinos.", e);
        }
        return treinos;
    }

    @Override
    public Optional<Treino> findById(long id) {
        String sql = "SELECT id, aluno_id, grupo_muscular, exercicio, repeticoes, peso, data FROM treinos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar treino por id.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Treino> findByAlunoId(long alunoId) {
        String sql = "SELECT id, aluno_id, grupo_muscular, exercicio, repeticoes, peso, data FROM treinos WHERE aluno_id = ?";
        List<Treino> treinos = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, alunoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) treinos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar treinos por aluno.", e);
        }
        return treinos;
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM treinos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar treino.", e);
        }
    }

    @Override
    public void update(Treino treino) {
        String sql = "UPDATE treinos SET aluno_id = ?, grupo_muscular = ?, exercicio = ?, repeticoes = ?, peso = ?, data = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, treino.getAlunoId());
            stmt.setString(2, treino.getGrupoMuscular());
            stmt.setString(3, treino.getExercicio());
            stmt.setInt(4, treino.getRepeticoes());
            stmt.setDouble(5, treino.getPeso());
            stmt.setString(6, treino.getData().toString());
            stmt.setLong(7, treino.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar treino.", e);
        }
    }

    private Treino mapRow(ResultSet rs) throws SQLException {
        Treino treino = new Treino.Builder()
                .alunoId(rs.getLong("aluno_id"))
                .grupoMuscular(rs.getString("grupo_muscular"))
                .exercicio(rs.getString("exercicio"))
                .repeticoes(rs.getInt("repeticoes"))
                .peso(rs.getDouble("peso"))
                .data(LocalDate.parse(rs.getString("data")))
                .build();
        treino.setId(rs.getLong("id"));
        return treino;
    }
}
