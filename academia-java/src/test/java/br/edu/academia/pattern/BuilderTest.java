package br.edu.academia.pattern;

import br.edu.academia.domain.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Builder (incluindo CRTP para method chaining).
 */
class BuilderTest {

    // ── Aluno Builder ────────────────────────────────────────────────────────

    @Test
    void alunoBuilderCriaObjetoCorreto() {
        Aluno aluno = new Aluno.Builder()
                .nome("Maria Silva")
                .dataNascimento(LocalDate.of(2000, 3, 15))
                .email("maria@email.com")
                .matricula(1234)
                .login("marialog")
                .senhaHash("hash123")
                .build();

        assertEquals("Maria Silva", aluno.getNome());
        assertEquals("maria@email.com", aluno.getEmail());
        assertEquals(1234, aluno.getMatricula());
        assertEquals("marialog", aluno.getLogin());
        assertEquals("hash123", aluno.getSenhaHash());
    }

    @Test
    void alunoBuilderSemNomeLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Aluno.Builder()
                        .dataNascimento(LocalDate.of(2000,1,1))
                        .email("a@e.com").matricula(1).login("log").senhaHash("h")
                        .build());
    }

    @Test
    void alunoBuilderSemLoginLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Aluno.Builder()
                        .nome("X").dataNascimento(LocalDate.of(2000,1,1))
                        .email("a@e.com").matricula(1).senhaHash("h")
                        .build());
    }

    @Test
    void alunoBuilderSemSenhaHashLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Aluno.Builder()
                        .nome("X").dataNascimento(LocalDate.of(2000,1,1))
                        .email("a@e.com").matricula(1).login("log")
                        .build());
    }

    @Test
    void alunoBuilderSemMatriculaLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Aluno.Builder()
                        .nome("X").dataNascimento(LocalDate.of(2000,1,1))
                        .email("a@e.com").login("log").senhaHash("h")
                        .build());
    }

    // ── Professor Builder ────────────────────────────────────────────────────

    @Test
    void professorBuilderCriaObjetoCorreto() {
        Professor prof = new Professor.Builder()
                .nome("Dr Carlos")
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .email("carlos@email.com")
                .login("drcarlos")
                .senhaHash("hash")
                .build();

        assertEquals("Dr Carlos", prof.getNome());
        assertEquals("drcarlos", prof.getLogin());
    }

    @Test
    void professorBuilderSemCampoObrigatorioLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Professor.Builder().nome("X").build());
    }

    // ── Turma Builder ────────────────────────────────────────────────────────

    @Test
    void turmaBuilderCriaObjetoCorreto() {
        Turma turma = new Turma.Builder()
                .nome("Musculacao A")
                .professorId(5L)
                .horario("08:00")
                .build();

        assertEquals("Musculacao A", turma.getNome());
        assertEquals(5L, turma.getProfessorId());
        assertEquals("08:00", turma.getHorario());
    }

    @Test
    void turmaBuilderSemNomeLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Turma.Builder().professorId(1L).horario("08:00").build());
    }

    @Test
    void turmaBuilderSemHorarioLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Turma.Builder().nome("T").professorId(1L).build());
    }

    // ── Treino Builder ───────────────────────────────────────────────────────

    @Test
    void treinoBuilderCriaObjetoCorreto() {
        Treino treino = new Treino.Builder()
                .alunoId(1L)
                .grupoMuscular("Peitoral")
                .exercicio("Supino")
                .repeticoes(12)
                .peso(60.0)
                .data(LocalDate.now())
                .build();

        assertEquals("Peitoral", treino.getGrupoMuscular());
        assertEquals(12, treino.getRepeticoes());
        assertEquals(60.0, treino.getPeso());
    }

    @Test
    void treinoBuilderSemAlunoIdLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Treino.Builder()
                        .grupoMuscular("P").exercicio("E").repeticoes(1).peso(10).data(LocalDate.now())
                        .build());
    }

    @Test
    void treinoBuilderPesoNegativoLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Treino.Builder()
                        .alunoId(1L).grupoMuscular("P").exercicio("E").repeticoes(1).peso(-1.0).data(LocalDate.now())
                        .build());
    }

    @Test
    void treinoBuilderRepeticoesZeroLancaIllegalState() {
        assertThrows(IllegalStateException.class, () ->
                new Treino.Builder()
                        .alunoId(1L).grupoMuscular("P").exercicio("E").repeticoes(0).peso(10).data(LocalDate.now())
                        .build());
    }

    // ── CRTP method chaining ─────────────────────────────────────────────────

    @Test
    void crtpAlunoBuilderRetornaTipoCorretoAoEncadear() {
        Aluno.Builder builder = new Aluno.Builder();
        Object result = builder.nome("X");

        assertInstanceOf(Aluno.Builder.class, result,
                "nome() deve retornar Aluno.Builder (CRTP), nao BuilderBase");
    }

    @Test
    void crtpProfessorBuilderRetornaTipoCorretoAoEncadear() {
        Professor.Builder builder = new Professor.Builder();
        Object result = builder.nome("X");

        assertInstanceOf(Professor.Builder.class, result);
    }
}
