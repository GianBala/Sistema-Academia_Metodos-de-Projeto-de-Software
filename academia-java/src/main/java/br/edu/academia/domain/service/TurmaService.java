package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Turma;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.repository.TurmaRepository;

import java.util.List;
import java.util.Optional;

public class TurmaService {

    private final TurmaRepository repository;
    private final HistoricoOperacoes historico;
    private final Logger logger;

    public TurmaService(TurmaRepository repository, HistoricoOperacoes historico, Logger logger) {
        this.repository = repository;
        this.historico = historico;
        this.logger = logger;
    }

    public Turma cadastrar(String nome, long professorId, String horario) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da turma é obrigatório.");
        if (professorId <= 0) throw new IllegalArgumentException("Professor é obrigatório.");
        if (horario == null || horario.isBlank()) throw new IllegalArgumentException("Horário é obrigatório.");

        Turma turma = new Turma.Builder()
                .nome(nome).professorId(professorId).horario(horario).build();
        repository.save(turma);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO,
                "Cadastro de turma: " + turma.getNome(),
                () -> repository.delete(turma.getId())
        ));

        logger.info("Turma cadastrada: " + turma.getNome());
        return turma;
    }

    /**
     * Atualiza uma turma e registra memento para desfazer a última atualização.
     */
    public Turma atualizar(long id, String nome, long professorId, String horario) {
        Turma anterior = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada: id=" + id));

        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome da turma é obrigatório.");
        if (horario == null || horario.isBlank()) throw new IllegalArgumentException("Horário é obrigatório.");

        Turma atualizada = new Turma.Builder()
                .nome(nome).professorId(professorId).horario(horario).build();
        atualizada.setId(id);
        repository.update(atualizada);

        // Memento — desfazer atualização restaura o estado anterior da turma
        historico.registrar(new OperacaoMemento(
                TipoOperacao.ATUALIZACAO,
                "Atualizacao de turma: " + anterior.getNome(),
                () -> repository.update(anterior)
        ));

        logger.info("Turma atualizada: id=" + id);
        return atualizada;
    }

    public void deletar(long id) {
        Turma turma = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada: id=" + id));
        repository.delete(id);
        logger.info("Turma deletada: " + turma.getNome());
    }

    public void adicionarAluno(long turmaId, long alunoId) {
        repository.findById(turmaId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada: id=" + turmaId));
        repository.addAluno(turmaId, alunoId);
        logger.info("Aluno " + alunoId + " adicionado à turma " + turmaId);
    }

    public void removerAluno(long turmaId, long alunoId) {
        repository.removeAluno(turmaId, alunoId);
        logger.info("Aluno " + alunoId + " removido da turma " + turmaId);
    }

    public List<Long> listarAlunosDaTurma(long turmaId) {
        return repository.findAlunoIds(turmaId);
    }

    public List<Turma> listarTurmasDoProfessor(long professorId) {
        return repository.findByProfessorId(professorId);
    }

    public Optional<Turma> buscarPorId(long id) { return repository.findById(id); }
    public List<Turma> listarTodos() { return repository.findAll(); }
}
