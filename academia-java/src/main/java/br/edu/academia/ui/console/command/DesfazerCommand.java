package br.edu.academia.ui.console.command;

import br.edu.academia.domain.memento.CadastroMemento;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.ui.console.ConsoleUtils;

import java.util.Optional;

public class DesfazerCommand implements Command {

    private final HistoricoOperacoes historico;
    private final ConsoleUtils console;

    public DesfazerCommand(HistoricoOperacoes historico, ConsoleUtils console) {
        this.historico = historico;
        this.console = console;
    }

    @Override
    public void execute() {
        if (!historico.temHistorico()) {
            System.out.println("Nenhuma operacao para desfazer.");
            console.waitForEnter();
            return;
        }

        Optional<CadastroMemento> desfeito = historico.desfazer();
        desfeito.ifPresent(m ->
                System.out.println("Cadastro desfeito: " + m.getDescricao()));
        console.waitForEnter();
    }
}
