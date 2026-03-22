package br.edu.academia.domain.command;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.entity.AlunoMemento;
import br.edu.academia.domain.repository.AlunoRepository;
import br.edu.academia.domain.service.AcessoService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.infrastructure.log.Logger;

public class AtualizarAlunoCommand implements Command {

    private final AlunoService alunoService;
    private final AlunoRepository alunoRepository;
    private final Logger logger;
    private final AcessoService acessoService;
    private final long id;
    private final String nome;
    private final String dataNascimento;
    private final String email;

    private AlunoMemento mementoAnterior;
    private Aluno resultado;

    public AtualizarAlunoCommand(AlunoService alunoService, AlunoRepository alunoRepository,
                                  Logger logger, AcessoService acessoService,
                                  long id, String nome, String dataNascimento, String email) {
        this.alunoService = alunoService;
        this.alunoRepository = alunoRepository;
        this.logger = logger;
        this.acessoService = acessoService;
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    @Override
    public void execute() {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno nao encontrado com id: " + id));
        mementoAnterior = aluno.criarMemento();

        resultado = alunoService.atualizar(id, nome, dataNascimento, email);
        logger.info("Aluno atualizado: id=" + id);
        acessoService.registrarOperacao("ATUALIZACAO", "ALUNO");
    }

    public void undo() {
        if (mementoAnterior == null) {
            throw new IllegalStateException("Nenhuma operacao para desfazer.");
        }
        Aluno aluno = alunoRepository.findById(mementoAnterior.getId())
                .orElseThrow(() -> new IllegalArgumentException("Aluno nao encontrado para desfazer."));
        aluno.restaurarMemento(mementoAnterior);
        alunoRepository.update(aluno);
        logger.info("Atualizacao de aluno desfeita: id=" + mementoAnterior.getId());
    }

    public Aluno getResultado() {
        return resultado;
    }
}
