package br.edu.academia.domain.report;

import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.repository.LoginHistoryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Padrão Template Method — define o esqueleto do algoritmo de geração de relatório.
 * Subclasses implementam os passos abstratos para formatar em HTML ou PDF.
 */
public abstract class RelatorioTemplate {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Template Method — algoritmo fixo de geração de relatório.
     * Subclasses não podem sobrescrever este método.
     */
    public final String gerarRelatorio(AcademiaFacade facade, LoginHistoryRepository loginHistory) {
        String cabecalho = gerarCabecalho(LocalDateTime.now().format(FMT));
        String entidades = gerarSecaoEntidades(facade.contarEntidadesPorTipo());
        String acessos   = gerarSecaoAcessos(loginHistory.loginsPorTipo(),
                                              loginHistory.ultimosLogins(10));
        String rodape    = gerarRodape();
        return montarDocumento(cabecalho, entidades, acessos, rodape);
    }

    /** Gera o cabeçalho do relatório com título e data. */
    protected abstract String gerarCabecalho(String dataHora);

    /** Gera a seção de contagem de entidades cadastradas. */
    protected abstract String gerarSecaoEntidades(Map<String, Integer> contagens);

    /** Gera a seção de estatísticas de acesso dos usuários. */
    protected abstract String gerarSecaoAcessos(Map<String, Long> loginsPorTipo,
                                                List<String> ultimosLogins);

    /** Gera o rodapé do relatório. */
    protected abstract String gerarRodape();

    /** Monta o documento final combinando todas as partes. */
    protected abstract String montarDocumento(String cabecalho, String entidades,
                                              String acessos, String rodape);
}
