package br.edu.academia.domain.report;

import br.edu.academia.domain.service.AcessoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GeradorRelatorio {

    private static final String[] TIPOS_ENTIDADE = {"ALUNO", "PROFESSOR", "ATENDENTE", "ADMINISTRADOR"};
    private static final String[] TIPOS_OPERACAO = {"CADASTRO", "LISTAGEM"};
    private static final DateTimeFormatter FILENAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final AcessoService acessoService;
    private final String outputDir;

    protected GeradorRelatorio(AcessoService acessoService) {
        this(acessoService, "relatorios");
    }

    protected GeradorRelatorio(AcessoService acessoService, String outputDir) {
        this.acessoService = acessoService;
        this.outputDir = outputDir;
    }

    public final String gerarRelatorio() {
        Map<String, Map<String, Long>> dados = coletarDados();
        String cabecalho = formatarCabecalho();
        String corpo = formatarCorpo(dados);
        String rodape = formatarRodape();
        String conteudo = cabecalho + corpo + rodape;
        return escreverArquivo(conteudo);
    }

    protected Map<String, Map<String, Long>> coletarDados() {
        Map<String, Map<String, Long>> dados = new LinkedHashMap<>();
        for (String entidade : TIPOS_ENTIDADE) {
            Map<String, Long> operacoes = new LinkedHashMap<>();
            for (String operacao : TIPOS_OPERACAO) {
                operacoes.put(operacao, acessoService.contarPorOperacaoEEntidade(operacao, entidade));
            }
            dados.put(entidade, operacoes);
        }
        return dados;
    }

    protected abstract String formatarCabecalho();

    protected abstract String formatarCorpo(Map<String, Map<String, Long>> dados);

    protected abstract String formatarRodape();

    protected abstract String getExtensao();

    protected String escreverArquivo(String conteudo) {
        try {
            Path dir = Paths.get(outputDir);
            Files.createDirectories(dir);
            String timestamp = LocalDateTime.now().format(FILENAME_FORMATTER);
            String nomeArquivo = "relatorio_" + timestamp + getExtensao();
            Path arquivo = dir.resolve(nomeArquivo);
            Files.writeString(arquivo, conteudo);
            return arquivo.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever arquivo de relatorio.", e);
        }
    }
}
