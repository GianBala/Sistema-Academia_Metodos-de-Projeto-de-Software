package br.edu.academia.domain.report;

import br.edu.academia.domain.service.AcessoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GeradorRelatorioTxt extends GeradorRelatorio {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public GeradorRelatorioTxt(AcessoService acessoService) {
        super(acessoService);
    }

    GeradorRelatorioTxt(AcessoService acessoService, String outputDir) {
        super(acessoService, outputDir);
    }

    @Override
    protected String formatarCabecalho() {
        String timestamp = LocalDateTime.now().format(DISPLAY_FORMATTER);
        return "=== RELATORIO DE OPERACOES ===\n"
                + "Gerado em: " + timestamp + "\n\n"
                + String.format("%-20s | %10s | %10s%n", "Entidade", "Cadastros", "Listagens")
                + "-".repeat(47) + "\n";
    }

    @Override
    protected String formatarCorpo(Map<String, Map<String, Long>> dados) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Long>> entry : dados.entrySet()) {
            String entidade = entry.getKey();
            long cadastros = entry.getValue().getOrDefault("CADASTRO", 0L);
            long listagens = entry.getValue().getOrDefault("LISTAGEM", 0L);
            sb.append(String.format("%-20s | %10d | %10d%n", entidade, cadastros, listagens));
        }
        return sb.toString();
    }

    @Override
    protected String formatarRodape() {
        return "-".repeat(47) + "\n";
    }

    @Override
    protected String getExtensao() {
        return ".txt";
    }
}
