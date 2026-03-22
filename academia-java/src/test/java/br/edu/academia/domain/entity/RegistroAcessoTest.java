package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RegistroAcessoTest {

    @Test
    void deveCriarRegistroComCamposCorretos() {
        LocalDateTime agora = LocalDateTime.now();
        RegistroAcesso registro = new RegistroAcesso("CADASTRO", "ALUNO", agora);

        assertEquals("CADASTRO", registro.getTipoOperacao());
        assertEquals("ALUNO", registro.getTipoEntidade());
        assertEquals(agora, registro.getTimestamp());
    }

    @Test
    void deveIniciarIdComZero() {
        RegistroAcesso registro = new RegistroAcesso("LISTAGEM", "PROFESSOR", LocalDateTime.now());
        assertEquals(0, registro.getId());
    }

    @Test
    void devePermitirSetId() {
        RegistroAcesso registro = new RegistroAcesso("CADASTRO", "ATENDENTE", LocalDateTime.now());
        registro.setId(42L);
        assertEquals(42L, registro.getId());
    }
}
