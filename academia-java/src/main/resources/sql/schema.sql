-- ================================================================
-- Schema do Sistema Academia
-- NOTA: Se já existir um banco de dados anterior (data/academia.db),
-- delete-o antes de executar para aplicar o novo schema completo.
-- ================================================================

CREATE TABLE IF NOT EXISTS administradores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    dt_nascimento TEXT NOT NULL,
    email TEXT NOT NULL,
    login TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS alunos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    dt_nascimento TEXT NOT NULL,
    email TEXT NOT NULL,
    matricula INTEGER NOT NULL UNIQUE,
    login TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS professores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    dt_nascimento TEXT NOT NULL,
    email TEXT NOT NULL,
    login TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS atendentes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    dt_nascimento TEXT NOT NULL,
    email TEXT NOT NULL,
    login TEXT NOT NULL UNIQUE,
    senha_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS turmas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    professor_id INTEGER NOT NULL,
    horario TEXT NOT NULL,
    FOREIGN KEY (professor_id) REFERENCES professores(id)
);

CREATE TABLE IF NOT EXISTS turma_alunos (
    turma_id INTEGER NOT NULL,
    aluno_id INTEGER NOT NULL,
    PRIMARY KEY (turma_id, aluno_id),
    FOREIGN KEY (turma_id) REFERENCES turmas(id),
    FOREIGN KEY (aluno_id) REFERENCES alunos(id)
);

CREATE TABLE IF NOT EXISTS treinos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    aluno_id INTEGER NOT NULL,
    grupo_muscular TEXT NOT NULL,
    exercicio TEXT NOT NULL,
    repeticoes INTEGER NOT NULL,
    peso REAL NOT NULL,
    data TEXT NOT NULL,
    FOREIGN KEY (aluno_id) REFERENCES alunos(id)
);

CREATE TABLE IF NOT EXISTS login_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    login TEXT NOT NULL,
    tipo_entidade TEXT NOT NULL,
    data_hora TEXT NOT NULL
);
