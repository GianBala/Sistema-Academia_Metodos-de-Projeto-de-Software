package br.edu.academia.testutil;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.repository.*;
import br.edu.academia.domain.service.MatriculaGenerator;
import br.edu.academia.infrastructure.security.PasswordHasher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Stubs e helpers reutilizaveis para todos os testes.
 * Sem dependencia de frameworks de mock — apenas implementacoes in-memory.
 */
public final class TestStubs {

    private TestStubs() {}

    // ── Constantes de dados validos ────────────────────────────────────────────

    /** Data de nascimento valida (30 anos atras) — sempre maior de 18. */
    public static final String VALID_DATE =
            LocalDate.now().minusYears(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    public static final String VALID_EMAIL    = "test@email.com";
    public static final String VALID_LOGIN    = "testlogin";   // sem numeros, <= 12 chars
    public static final String VALID_PASSWORD = "Senha@abc1";  // upper + lower + digit + special

    // ── Logger no-op ───────────────────────────────────────────────────────────

    public static class NoOpLogger implements Logger {
        @Override public void info(String msg)  {}
        @Override public void warn(String msg)  {}
        @Override public void error(String msg) {}
        @Override public void debug(String msg) {}
    }

    // ── FakePasswordHasher ─────────────────────────────────────────────────────

    public static class FakePasswordHasher implements PasswordHasher {
        @Override
        public String hash(String plaintext) {
            return "hashed_" + plaintext;
        }
        @Override
        public boolean verify(String plaintext, String hash) {
            return ("hashed_" + plaintext).equals(hash);
        }
    }

    // ── FixedMatriculaGenerator ────────────────────────────────────────────────

    public static class FixedMatriculaGenerator implements MatriculaGenerator {
        private final int value;
        public FixedMatriculaGenerator(int value) { this.value = value; }
        @Override public int generate() { return value; }
    }

    // ── InMemoryAlunoRepository ────────────────────────────────────────────────

    public static class InMemoryAlunoRepository implements AlunoRepository {
        private final Map<Long, Aluno> store = new LinkedHashMap<>();
        private final AtomicLong nextId = new AtomicLong(1);

        @Override public void save(Aluno a)   { a.setId(nextId.getAndIncrement()); store.put(a.getId(), a); }
        @Override public void update(Aluno a) { store.put(a.getId(), a); }
        @Override public void delete(long id) { store.remove(id); }
        @Override public List<Aluno> findAll()               { return new ArrayList<>(store.values()); }
        @Override public Optional<Aluno> findById(long id)   { return Optional.ofNullable(store.get(id)); }
        @Override public Optional<Aluno> findByMatricula(int m) {
            return store.values().stream().filter(a -> a.getMatricula() == m).findFirst();
        }
        @Override public Optional<Aluno> findByLogin(String login) {
            return store.values().stream().filter(a -> a.getLogin().equals(login)).findFirst();
        }
    }

    // ── InMemoryProfessorRepository ────────────────────────────────────────────

    public static class InMemoryProfessorRepository implements ProfessorRepository {
        private final Map<Long, Professor> store = new LinkedHashMap<>();
        private final AtomicLong nextId = new AtomicLong(1);

        @Override public void save(Professor p)   { p.setId(nextId.getAndIncrement()); store.put(p.getId(), p); }
        @Override public void update(Professor p) { store.put(p.getId(), p); }
        @Override public void delete(long id)     { store.remove(id); }
        @Override public List<Professor> findAll()               { return new ArrayList<>(store.values()); }
        @Override public Optional<Professor> findById(long id)   { return Optional.ofNullable(store.get(id)); }
        @Override public Optional<Professor> findByLogin(String login) {
            return store.values().stream().filter(p -> p.getLogin().equals(login)).findFirst();
        }
    }

    // ── InMemoryAtendenteRepository ────────────────────────────────────────────

    public static class InMemoryAtendenteRepository implements AtendenteRepository {
        private final Map<Long, Atendente> store = new LinkedHashMap<>();
        private final AtomicLong nextId = new AtomicLong(1);

        @Override public void save(Atendente a)   { a.setId(nextId.getAndIncrement()); store.put(a.getId(), a); }
        @Override public void update(Atendente a) { store.put(a.getId(), a); }
        @Override public void delete(long id)     { store.remove(id); }
        @Override public List<Atendente> findAll()               { return new ArrayList<>(store.values()); }
        @Override public Optional<Atendente> findById(long id)   { return Optional.ofNullable(store.get(id)); }
        @Override public Optional<Atendente> findByLogin(String login) {
            return store.values().stream().filter(a -> a.getLogin().equals(login)).findFirst();
        }
    }

    // ── InMemoryAdministradorRepository ───────────────────────────────────────

    public static class InMemoryAdministradorRepository implements AdministradorRepository {
        private final Map<Long, Administrador> store = new LinkedHashMap<>();
        private final AtomicLong nextId = new AtomicLong(1);

        @Override public void save(Administrador a)   { a.setId(nextId.getAndIncrement()); store.put(a.getId(), a); }
        @Override public void update(Administrador a) { store.put(a.getId(), a); }
        @Override public void delete(long id)         { store.remove(id); }
        @Override public List<Administrador> findAll()               { return new ArrayList<>(store.values()); }
        @Override public Optional<Administrador> findById(long id)   { return Optional.ofNullable(store.get(id)); }
        @Override public Optional<Administrador> findByLogin(String login) {
            return store.values().stream().filter(a -> a.getLogin().equals(login)).findFirst();
        }
    }

    // ── InMemoryTurmaRepository ────────────────────────────────────────────────

    public static class InMemoryTurmaRepository implements TurmaRepository {
        private final Map<Long, Turma> store = new LinkedHashMap<>();
        private final Map<Long, List<Long>> turmaAlunos = new LinkedHashMap<>();
        private final AtomicLong nextId = new AtomicLong(1);

        @Override public void save(Turma t)   { t.setId(nextId.getAndIncrement()); store.put(t.getId(), t); }
        @Override public void update(Turma t) { store.put(t.getId(), t); }
        @Override public void delete(long id) { store.remove(id); turmaAlunos.remove(id); }
        @Override public List<Turma> findAll()             { return new ArrayList<>(store.values()); }
        @Override public Optional<Turma> findById(long id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<Turma> findByProfessorId(long professorId) {
            return store.values().stream().filter(t -> t.getProfessorId() == professorId).collect(Collectors.toList());
        }
        @Override public void addAluno(long turmaId, long alunoId) {
            turmaAlunos.computeIfAbsent(turmaId, k -> new ArrayList<>()).add(alunoId);
        }
        @Override public void removeAluno(long turmaId, long alunoId) {
            List<Long> list = turmaAlunos.get(turmaId);
            if (list != null) list.remove(alunoId);
        }
        @Override public List<Long> findAlunoIds(long turmaId) {
            return turmaAlunos.getOrDefault(turmaId, Collections.emptyList());
        }
    }

    // ── InMemoryTreinoRepository ───────────────────────────────────────────────

    public static class InMemoryTreinoRepository implements TreinoRepository {
        private final Map<Long, Treino> store = new LinkedHashMap<>();
        private final AtomicLong nextId = new AtomicLong(1);

        @Override public void save(Treino t)   { t.setId(nextId.getAndIncrement()); store.put(t.getId(), t); }
        @Override public void update(Treino t) { store.put(t.getId(), t); }
        @Override public void delete(long id)  { store.remove(id); }
        @Override public List<Treino> findAll()             { return new ArrayList<>(store.values()); }
        @Override public Optional<Treino> findById(long id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<Treino> findByAlunoId(long alunoId) {
            return store.values().stream().filter(t -> t.getAlunoId() == alunoId).collect(Collectors.toList());
        }
    }

    // ── InMemoryLoginHistoryRepository ────────────────────────────────────────

    public static class InMemoryLoginHistoryRepository implements LoginHistoryRepository {
        private final List<String[]> records = new ArrayList<>();

        @Override
        public void registrar(String login, String tipoEntidade, LocalDateTime dataHora) {
            records.add(new String[]{login, tipoEntidade, dataHora.toString()});
        }
        @Override
        public long contarLogins(String tipoEntidade) {
            return records.stream().filter(r -> r[1].equals(tipoEntidade)).count();
        }
        @Override
        public List<String> ultimosLogins(int limite) {
            int from = Math.max(0, records.size() - limite);
            return records.subList(from, records.size()).stream()
                    .map(r -> r[0] + " [" + r[1] + "] @ " + r[2])
                    .collect(Collectors.toList());
        }
        @Override
        public Map<String, Long> loginsPorTipo() {
            return records.stream().collect(
                    Collectors.groupingBy(r -> r[1], Collectors.counting()));
        }
        public int total() { return records.size(); }
    }

    // ── Entity helpers ─────────────────────────────────────────────────────────

    public static Aluno criarAluno(String nome, int matricula) {
        return new Aluno.Builder()
                .nome(nome)
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .email(nome.toLowerCase().replace(" ", "") + "@email.com")
                .matricula(matricula)
                .login("login" + matricula)
                .senhaHash("hash_" + matricula)
                .build();
    }

    public static Professor criarProfessor(String nome) {
        return new Professor.Builder()
                .nome(nome)
                .dataNascimento(LocalDate.of(1985, 6, 15))
                .email(nome.toLowerCase().replace(" ", "") + "@email.com")
                .login("prof" + nome.length())
                .senhaHash("hash_prof")
                .build();
    }

    public static Atendente criarAtendente(String nome) {
        return new Atendente.Builder()
                .nome(nome)
                .dataNascimento(LocalDate.of(1990, 3, 20))
                .email(nome.toLowerCase().replace(" ", "") + "@email.com")
                .login("atd" + nome.length())
                .senhaHash("hash_atd")
                .build();
    }

    public static Administrador criarAdministrador(String nome) {
        return new Administrador.Builder()
                .nome(nome)
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .email(nome.toLowerCase().replace(" ", "") + "@email.com")
                .login("adm" + nome.length())
                .senhaHash("hash_adm")
                .build();
    }
}
