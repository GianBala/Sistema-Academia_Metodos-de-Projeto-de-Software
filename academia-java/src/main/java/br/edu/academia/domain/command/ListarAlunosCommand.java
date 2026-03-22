package br.edu.academia.domain.command;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.service.AcessoService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.infrastructure.log.Logger;

import java.util.List;

public class ListarAlunosCommand implements Command {

    private final AlunoService alunoService;
    private final Logger logger;
    private final AcessoService acessoService;

    private List<Aluno> resultado;

    public ListarAlunosCommand(AlunoService alunoService, Logger logger, AcessoService acessoService) {
        this.alunoService = alunoService;
        this.logger = logger;
        this.acessoService = acessoService;
    }

    @Override
    public void execute() {
        logger.info("Listando alunos");
        acessoService.registrarOperacao("LISTAGEM", "ALUNO");
        resultado = alunoService.listarTodos();
    }

    public List<Aluno> getResultado() {
        return resultado;
    }
}
