package br.edu.academia.domain.service;

/**
 * @deprecated Substituído pelo padrão Template Method em {@code domain.report}.
 * Use {@link br.edu.academia.domain.report.HTMLRelatorio} ou
 * {@link br.edu.academia.domain.report.PDFRelatorio} com
 * {@link br.edu.academia.domain.report.RelatorioTemplate}.
 *
 * Mantido apenas para não quebrar compilações que ainda referenciem esta classe.
 */
@Deprecated
public class RelatorioService {

    private final String titulo;

    public RelatorioService(String titulo) {
        this.titulo = titulo;
    }

    public String gerarRelatorio() {
        return "Relatório '" + titulo + "' foi movido para domain.report (Template Method).";
    }
}
