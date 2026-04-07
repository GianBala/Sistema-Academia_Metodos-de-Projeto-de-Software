package br.edu.academia.domain.report;

import java.util.List;
import java.util.Map;

/**
 * Relatório em formato PDF (simulado em texto) — implementação concreta do Template Method.
 * Usa formatação textual estruturada que simula a aparência de um PDF.
 */
public class PDFRelatorio extends RelatorioTemplate {

    private static final String LINHA = "=".repeat(60);
    private static final String LINHA_FINA = "-".repeat(60);

    @Override
    protected String gerarCabecalho(String dataHora) {
        return LINHA + "\n"
                + centralizar("SISTEMA ACADEMIA - RELATORIO") + "\n"
                + centralizar("Gerado em: " + dataHora) + "\n"
                + LINHA + "\n";
    }

    @Override
    protected String gerarSecaoEntidades(Map<String, Integer> contagens) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(centralizar("ENTIDADES CADASTRADAS")).append("\n")
          .append(LINHA_FINA).append("\n");
        contagens.forEach((tipo, qtd) ->
                sb.append(String.format("  %-25s %5d\n", tipo + ":", qtd)));
        sb.append(LINHA_FINA).append("\n");
        return sb.toString();
    }

    @Override
    protected String gerarSecaoAcessos(Map<String, Long> loginsPorTipo, List<String> ultimosLogins) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(centralizar("ESTATISTICAS DE ACESSO")).append("\n")
          .append(LINHA_FINA).append("\n");

        sb.append("Total de Logins por Tipo:\n");
        if (loginsPorTipo.isEmpty()) {
            sb.append("  Nenhum acesso registrado.\n");
        } else {
            loginsPorTipo.forEach((tipo, total) ->
                    sb.append(String.format("  %-25s %5d\n", tipo + ":", total)));
        }

        sb.append("\nUltimos Acessos:\n");
        if (ultimosLogins.isEmpty()) {
            sb.append("  Nenhum acesso registrado.\n");
        } else {
            ultimosLogins.forEach(log -> sb.append("  ").append(log).append("\n"));
        }
        sb.append(LINHA_FINA).append("\n");
        return sb.toString();
    }

    @Override
    protected String gerarRodape() {
        return "\n" + LINHA + "\n"
                + centralizar("Sistema Academia (c) 2025") + "\n"
                + LINHA + "\n";
    }

    @Override
    protected String montarDocumento(String cabecalho, String entidades, String acessos, String rodape) {
        return cabecalho + entidades + acessos + rodape;
    }

    private String centralizar(String texto) {
        int padding = Math.max(0, (60 - texto.length()) / 2);
        return " ".repeat(padding) + texto;
    }
}
