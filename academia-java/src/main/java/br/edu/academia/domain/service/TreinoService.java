package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Treino;
import br.edu.academia.domain.logging.Logger;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.memento.OperacaoMemento;
import br.edu.academia.domain.memento.TipoOperacao;
import br.edu.academia.domain.repository.TreinoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TreinoService {

    private final TreinoRepository repository;
    private final HistoricoOperacoes historico;
    private final Logger logger;

    public TreinoService(TreinoRepository repository, HistoricoOperacoes historico, Logger logger) {
        this.repository = repository;
        this.historico = historico;
        this.logger = logger;
    }

    public Treino cadastrar(long alunoId, String grupoMuscular, String exercicio,
                            int repeticoes, double peso) {
        Treino treino = new Treino.Builder()
                .alunoId(alunoId)
                .grupoMuscular(grupoMuscular)
                .exercicio(exercicio)
                .repeticoes(repeticoes)
                .peso(peso)
                .data(LocalDate.now())
                .build();
        repository.save(treino);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.CRIACAO,
                "Cadastro de treino: " + exercicio + " para aluno " + alunoId,
                () -> repository.delete(treino.getId())
        ));

        logger.info("Treino cadastrado para aluno " + alunoId + ": " + exercicio);
        return treino;
    }

    public Treino atualizar(long id, String grupoMuscular, String exercicio,
                            int repeticoes, double peso) {
        Treino anterior = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado: id=" + id));

        Treino atualizado = new Treino.Builder()
                .alunoId(anterior.getAlunoId())
                .grupoMuscular(grupoMuscular)
                .exercicio(exercicio)
                .repeticoes(repeticoes)
                .peso(peso)
                .data(anterior.getData())
                .build();
        atualizado.setId(id);
        repository.update(atualizado);

        historico.registrar(new OperacaoMemento(
                TipoOperacao.ATUALIZACAO,
                "Atualizacao de treino: " + anterior.getExercicio(),
                () -> repository.update(anterior)
        ));

        logger.info("Treino atualizado: id=" + id);
        return atualizado;
    }

    public void deletar(long id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treino não encontrado: id=" + id));
        repository.delete(id);
        logger.info("Treino deletado: id=" + id);
    }

    public List<Treino> listarPorAluno(long alunoId) {
        return repository.findByAlunoId(alunoId);
    }

    public Optional<Treino> buscarPorId(long id) { return repository.findById(id); }
    public List<Treino> listarTodos() { return repository.findAll(); }
}
