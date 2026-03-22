package br.edu.academia.domain.command;

public class CommandHistory {

    private AtualizarAlunoCommand ultimaAtualizacao;

    public void executeCommand(Command command) {
        command.execute();
        if (command instanceof AtualizarAlunoCommand atualizarCmd) {
            ultimaAtualizacao = atualizarCmd;
        }
    }

    public void undoUltimaAtualizacao() {
        if (ultimaAtualizacao == null) {
            throw new IllegalStateException("Nenhuma atualizacao para desfazer.");
        }
        ultimaAtualizacao.undo();
        ultimaAtualizacao = null;
    }

    public boolean temAtualizacaoParaDesfazer() {
        return ultimaAtualizacao != null;
    }
}
