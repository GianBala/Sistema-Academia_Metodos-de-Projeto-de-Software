package br.edu.academia.infrastructure.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:data/academia.db";
    private static volatile DatabaseConnection instance;
    private final Connection connection;

    private DatabaseConnection() {
        this(DB_URL);
    }

    DatabaseConnection(String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            initializeSchema();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone nao permitido para Singleton.");
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexao com banco de dados.", e);
        }
    }

    private void initializeSchema() {
        String sql = loadSchemaFromResources();
        try (Statement stmt = connection.createStatement()) {
            for (String command : sql.split(";")) {
                String trimmed = command.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar schema do banco de dados.", e);
        }
    }

    private String loadSchemaFromResources() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("sql/schema.sql")) {
            if (is == null) {
                throw new RuntimeException("Arquivo schema.sql nao encontrado nos resources.");
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler schema.sql.", e);
        }
    }
}
