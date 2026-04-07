package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.memento.HistoricoOperacoes;
import br.edu.academia.testutil.TestStubs;
import br.edu.academia.testutil.TestStubs.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorServiceTest {

    private AdministradorService service;
    private InMemoryAdministradorRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAdministradorRepository();
        service    = new AdministradorService(repository,
                new FakePasswordHasher(),
                new HistoricoOperacoes(),
                new NoOpLogger());
    }

    @Test
    void deveCadastrarAdministradorComDadosValidos() {
        Administrador admin = service.cadastrar(
                "Admin", "01/01/1990", "admin@email.com", "adminlogin", "Senha123!");

        assertEquals("Admin", admin.getNome());
        assertEquals("adminlogin", admin.getLogin());
        assertTrue(admin.getSenhaHash().startsWith("hashed_"));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void deveRejeitarLoginComNumeros() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Admin", "01/01/1990", "a@e.com", "admin123", "Senha123!"));
    }

    @Test
    void deveRejeitarSenhaFraca() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Admin", "01/01/1990", "a@e.com", "admin", "abc"));
    }

    @Test
    void deveRejeitarLoginMuitoLongo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.cadastrar("Admin", "01/01/1990", "a@e.com",
                        "loginmuitolongo", "Senha123!"));
    }

    @Test
    void deveAtualizarAdministrador() {
        Administrador admin = service.cadastrar("Original", "01/01/1990",
                "orig@email.com", "origlogin", "Senha123!");

        Administrador atualizado = service.atualizar(admin.getId(), "Novo Nome",
                "01/01/1990", "novo@email.com", "novologin", "Senha123!");

        assertEquals("Novo Nome", atualizado.getNome());
    }

    @Test
    void deveDeletarAdministrador() {
        Administrador admin = service.cadastrar("Delete Me", "01/01/1990",
                "del@email.com", "dellogin", "Senha123!");

        service.deletar(admin.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deveLancarExcecaoAoAtualizarInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                service.atualizar(999L, "Nome", "01/01/1990",
                        "a@e.com", "login", "Senha123!"));
    }

    @Test
    void deveLancarExcecaoAoDeletarInexistente() {
        assertThrows(IllegalArgumentException.class, () -> service.deletar(999L));
    }
}
