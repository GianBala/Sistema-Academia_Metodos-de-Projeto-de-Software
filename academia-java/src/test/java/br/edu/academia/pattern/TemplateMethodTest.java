package br.edu.academia.pattern;

import br.edu.academia.domain.entity.*;
import br.edu.academia.domain.facade.AcademiaFacade;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.domain.report.HTMLRelatorio;
import br.edu.academia.domain.report.PDFRelatorio;
import br.edu.academia.domain.service.*;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Template Method:
 * 1. Entidade.infos() — algoritmo fixo + hook detalhesAdicionais()
 * 2. RelatorioTemplate.gerarRelatorio() — 5 passos abstratos
 */
class TemplateMethodTest {

    @BeforeEach
    @AfterEach
    void resetFacade() throws Exception {
        Field field = AcademiaFacade.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    // ── Entidade.infos() ─────────────────────────────────────────────────────

    @Test
    void infosContemCamposBase() {
        Aluno aluno = new Aluno.Builder()
                .nome("Maria Silva")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .email("maria@email.com")
                .matricula(1234)
                .login("marialog")
                .senhaHash("hash")
                .build();

        String infos = aluno.infos();

        assertTrue(infos.contains("Maria Silva"));
        assertTrue(infos.contains("maria@email.com"));
        assertTrue(infos.contains("marialog"));
        assertTrue(infos.contains("Aluno")); // Cargo
    }

    @Test
    void infosContemDetalhesAdicionaisDeAluno() {
        Aluno aluno = new Aluno.Builder()
                .nome("Carlos").dataNascimento(LocalDate.of(2000,1,1))
                .email("c@e.com").matricula(9999).login("log").senhaHash("h").build();

        assertTrue(aluno.infos().contains("9999"));
        assertTrue(aluno.infos().contains("Matricula"));
    }

    @Test
    void infosNaoExibeDetalhesVaziosParaProfessor() {
        Professor prof = new Professor.Builder()
                .nome("Dr Jose").dataNascimento(LocalDate.of(1980,1,1))
                .email("jose@e.com").login("drjose").senhaHash("h").build();

        String infos = prof.infos();

        // Professor.detalhesAdicionais() retorna "", entao infos nao deve terminar com " | "
        assertFalse(infos.endsWith(" | "), "infos() nao deve terminar com separador desnecessario");
    }

    @Test
    void infosEhFinalNaoPermiteSobrescrita() throws Exception {
        // Verifica que infos() e declarado como final em Entidade
        var method = Entidade.class.getDeclaredMethod("infos");
        int modifiers = method.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isFinal(modifiers),
                "infos() deve ser final para garantir o Template Method");
    }

    // ── RelatorioTemplate ────────────────────────────────────────────────────

    @Test
    void htmlRelatorioGeraDocumentoComTagsHtml() {
        AcademiaFacade facade = inicializarFacade();
        InMemoryLoginHistoryRepository hist = new InMemoryLoginHistoryRepository();

        String relatorio = new HTMLRelatorio().gerarRelatorio(facade, hist);

        assertTrue(relatorio.contains("<html"), "HTML deve conter tag <html");
        assertTrue(relatorio.contains("</html>"), "HTML deve fechar tag </html>");
    }

    @Test
    void pdfRelatorioGeraDocumentoComLinhasSeparadoras() {
        AcademiaFacade facade = inicializarFacade();
        InMemoryLoginHistoryRepository hist = new InMemoryLoginHistoryRepository();

        String relatorio = new PDFRelatorio().gerarRelatorio(facade, hist);

        assertTrue(relatorio.contains("===="), "PDF deve conter separadores '===='");
    }

    @Test
    void htmlEPdfGeramFormatosDiferentes() {
        AcademiaFacade facade = inicializarFacade();
        InMemoryLoginHistoryRepository hist = new InMemoryLoginHistoryRepository();

        String html = new HTMLRelatorio().gerarRelatorio(facade, hist);
        String pdf  = new PDFRelatorio().gerarRelatorio(facade, hist);

        assertNotEquals(html, pdf, "HTML e PDF devem gerar documentos diferentes");
    }

    @Test
    void htmlRelatorioContemSecaoEntidades() {
        AcademiaFacade facade = inicializarFacade();
        String relatorio = new HTMLRelatorio().gerarRelatorio(facade, new InMemoryLoginHistoryRepository());

        assertTrue(relatorio.toLowerCase().contains("entidades"),
                "Relatorio HTML deve conter secao de entidades");
    }

    // ── helper ───────────────────────────────────────────────────────────────

    private AcademiaFacade inicializarFacade() {
        var hist = new HistoricoOperacoes();
        var log  = new NoOpLogger();
        var hasher = new FakePasswordHasher();
        AcademiaFacade.init(
                new AlunoService(new InMemoryAlunoRepository(), new FixedMatriculaGenerator(1), hasher, hist, log),
                new ProfessorService(new InMemoryProfessorRepository(), hasher, hist, log),
                new AtendenteService(new InMemoryAtendenteRepository(), hasher, hist, log),
                new AdministradorService(new InMemoryAdministradorRepository(), hasher, hist, log),
                new TurmaService(new InMemoryTurmaRepository(), hist, log),
                new TreinoService(new InMemoryTreinoRepository(), hist, log)
        );
        return AcademiaFacade.getInstance();
    }
}
