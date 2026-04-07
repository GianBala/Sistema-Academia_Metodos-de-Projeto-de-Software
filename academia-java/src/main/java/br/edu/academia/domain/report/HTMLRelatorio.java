package br.edu.academia.domain.report;

import java.util.List;
import java.util.Map;

/**
 * Relatório em formato HTML — implementação concreta do Template Method.
 */
public class HTMLRelatorio extends RelatorioTemplate {

    @Override
    protected String gerarCabecalho(String dataHora) {
        return "<html><head><meta charset='UTF-8'><title>Relatorio Academia</title></head><body>\n"
                + "<h1>Sistema Academia - Relatorio</h1>\n"
                + "<p><strong>Gerado em:</strong> " + dataHora + "</p>\n";
    }

    @Override
    protected String gerarSecaoEntidades(Map<String, Integer> contagens) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Entidades Cadastradas</h2>\n<table border='1' cellpadding='5'>\n")
          .append("<tr><th>Tipo</th><th>Total</th></tr>\n");
        contagens.forEach((tipo, qtd) ->
                sb.append("<tr><td>").append(tipo).append("</td><td>").append(qtd).append("</td></tr>\n"));
        sb.append("</table>\n");
        return sb.toString();
    }

    @Override
    protected String gerarSecaoAcessos(Map<String, Long> loginsPorTipo, List<String> ultimosLogins) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Estatisticas de Acesso</h2>\n");

        sb.append("<h3>Total de Logins por Tipo</h3>\n<table border='1' cellpadding='5'>\n")
          .append("<tr><th>Tipo</th><th>Total Logins</th></tr>\n");
        if (loginsPorTipo.isEmpty()) {
            sb.append("<tr><td colspan='2'>Nenhum acesso registrado.</td></tr>\n");
        } else {
            loginsPorTipo.forEach((tipo, total) ->
                    sb.append("<tr><td>").append(tipo).append("</td><td>").append(total).append("</td></tr>\n"));
        }
        sb.append("</table>\n");

        sb.append("<h3>Ultimos Acessos</h3>\n<ul>\n");
        if (ultimosLogins.isEmpty()) {
            sb.append("<li>Nenhum acesso registrado.</li>\n");
        } else {
            ultimosLogins.forEach(log -> sb.append("<li>").append(log).append("</li>\n"));
        }
        sb.append("</ul>\n");
        return sb.toString();
    }

    @Override
    protected String gerarRodape() {
        return "<hr/><p><em>Sistema Academia &copy; 2025</em></p>\n</body></html>";
    }

    @Override
    protected String montarDocumento(String cabecalho, String entidades, String acessos, String rodape) {
        return cabecalho + entidades + acessos + rodape;
    }
}
