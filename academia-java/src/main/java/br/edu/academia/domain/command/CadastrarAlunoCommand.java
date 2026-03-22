package br.edu.academia.domain.command;

import br.edu.academia.domain.entity.Aluno;
import br.edu.academia.domain.service.AcessoService;
import br.edu.academia.domain.service.AlunoService;
import br.edu.academia.infrastructure.log.Logger;

public class CadastrarAlunoCommand implements Command {

    private final AlunoService alunoService;
    private final Logger logger;
    private final AcessoService acessoService;
    private final String nome;
    private final String dataNascimento;
    private final String email;

    private Aluno resultado;

    public CadastrarAlunoCommand(AlunoService alunoService, Logger logger, AcessoService acessoService,
                                  String nome, String dataNascimento, String email) {
        this.alunoService = alunoService;
        this.logger = logger;
        this.acessoService = acessoService;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    @Override
    public void execute() {
        resultado = alunoService.cadastrar(nome, dataNascimento, email);
        logger.info("Aluno cadastrado: " + nome);
        acessoService.registrarOperacao("CADASTRO", "ALUNO");
    }

    public Aluno getResultado() {
        return resultado;
    }
}
