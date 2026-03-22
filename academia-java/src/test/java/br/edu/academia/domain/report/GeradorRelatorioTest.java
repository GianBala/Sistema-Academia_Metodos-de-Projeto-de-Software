package br.edu.academia.domain.report;

import br.edu.academia.domain.entity.RegistroAcesso;
import br.edu.academia.domain.repository.RegistroAcessoRepository;
import br.edu.academia.domain.service.AcessoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GeradorRelatorioTest {

    @TempDir
    Path tempDir;

    private AcessoService acessoService;

    @BeforeEach
    void setUp() {
        acessoService = new AcessoService(new InMemoryRegistroAcessoRepository());
        acessoService.registrarOperacao("CADASTRO", "ALUNO");
        acessoService.registrarOperacao("CADASTRO", "ALUNO");
        acessoService.registrarOperacao("LISTAGEM", "ALUNO");
        acessoService.registrarOperacao("CADASTRO", "PROFESSOR");
    }

    @Test
    void deveGerarArquivoHtml() throws IOException {
        GeradorRelatorio gerador = new GeradorRelatorioHtml(acessoService, tempDir.toString());
        String caminho = gerador.gerarRelatorio();

        assertTrue(caminho.endsWith(".html"));
        Path arquivo = Path.of(caminho);
        assertTrue(Files.exists(arquivo));

        String conteudo = Files.readString(arquivo);
        assertTrue(conteudo.contains("<!DOCTYPE html>"));
        assertTrue(conteudo.contains("ALUNO"));
        assertTrue(conteudo.contains("PROFESSOR"));
        assertTrue(conteudo.contains(">2<"));
    }

    @Test
    void deveGerarArquivoTxt() throws IOException {
        GeradorRelatorio gerador = new GeradorRelatorioTxt(acessoService, tempDir.toString());
        String caminho = gerador.gerarRelatorio();

        assertTrue(caminho.endsWith(".txt"));
        Path arquivo = Path.of(caminho);
        assertTrue(Files.exists(arquivo));

        String conteudo = Files.readString(arquivo);
        assertTrue(conteudo.contains("RELATORIO DE OPERACOES"));
        assertTrue(conteudo.contains("ALUNO"));
        assertTrue(conteudo.contains("PROFESSOR"));
        assertTrue(conteudo.contains("Cadastros"));
        assertTrue(conteudo.contains("Listagens"));
    }

    @Test
    void htmlDeveConterEstruturaDaTabela() throws IOException {
        GeradorRelatorio gerador = new GeradorRelatorioHtml(acessoService, tempDir.toString());
        String caminho = gerador.gerarRelatorio();
        String conteudo = Files.readString(Path.of(caminho));

        assertTrue(conteudo.contains("<table>"));
        assertTrue(conteudo.contains("</table>"));
        assertTrue(conteudo.contains("<th>Entidade</th>"));
        assertTrue(conteudo.contains("<th>Cadastros</th>"));
        assertTrue(conteudo.contains("<th>Listagens</th>"));
    }

    private static class InMemoryRegistroAcessoRepository implements RegistroAcessoRepository {

        private final List<RegistroAcesso> registros = new ArrayList<>();
        private long nextId = 1;

        @Override
        public void save(RegistroAcesso registro) {
            registro.setId(nextId++);
            registros.add(registro);
        }

        @Override
        public List<RegistroAcesso> findAll() {
            return new ArrayList<>(registros);
        }

        @Override
        public Optional<RegistroAcesso> findById(long id) {
            return registros.stream().filter(r -> r.getId() == id).findFirst();
        }

        @Override
        public List<RegistroAcesso> findByTipoOperacao(String tipoOperacao) {
            return registros.stream()
                    .filter(r -> r.getTipoOperacao().equals(tipoOperacao))
                    .collect(Collectors.toList());
        }

        @Override
        public List<RegistroAcesso> findByTipoEntidade(String tipoEntidade) {
            return registros.stream()
                    .filter(r -> r.getTipoEntidade().equals(tipoEntidade))
                    .collect(Collectors.toList());
        }

        @Override
        public long countByTipoOperacaoAndTipoEntidade(String tipoOperacao, String tipoEntidade) {
            return registros.stream()
                    .filter(r -> r.getTipoOperacao().equals(tipoOperacao)
                            && r.getTipoEntidade().equals(tipoEntidade))
                    .count();
        }
    }
}
