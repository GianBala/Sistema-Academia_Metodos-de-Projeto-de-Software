package br.edu.academia.domain.report;

import br.edu.academia.domain.service.AcessoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GeradorRelatorioHtml extends GeradorRelatorio {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public GeradorRelatorioHtml(AcessoService acessoService) {
        super(acessoService);
    }

    GeradorRelatorioHtml(AcessoService acessoService, String outputDir) {
        super(acessoService, outputDir);
    }

    @Override
    protected String formatarCabecalho() {
        String timestamp = LocalDateTime.now().format(DISPLAY_FORMATTER);
        return "<!DOCTYPE html>\n<html>\n<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Relatorio Academia</title>\n"
                + "<style>table{border-collapse:collapse;}th,td{border:1px solid #333;padding:8px;}"
                + "th{background:#ddd;}</style>\n"
                + "</head>\n<body>\n"
                + "<h1>Relatorio de Operacoes</h1>\n"
                + "<p>Gerado em: " + timestamp + "</p>\n"
                + "<table>\n"
                + "<tr><th>Entidade</th><th>Cadastros</th><th>Listagens</th></tr>\n";
    }

    @Override
    protected String formatarCorpo(Map<String, Map<String, Long>> dados) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<String, Long>> entry : dados.entrySet()) {
            String entidade = entry.getKey();
            long cadastros = entry.getValue().getOrDefault("CADASTRO", 0L);
            long listagens = entry.getValue().getOrDefault("LISTAGEM", 0L);
            sb.append("<tr><td>").append(entidade).append("</td>")
              .append("<td>").append(cadastros).append("</td>")
              .append("<td>").append(listagens).append("</td></tr>\n");
        }
        return sb.toString();
    }

    @Override
    protected String formatarRodape() {
        return "</table>\n</body>\n</html>\n";
    }

    @Override
    protected String getExtensao() {
        return ".html";
    }
}
