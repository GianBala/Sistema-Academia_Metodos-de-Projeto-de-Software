package br.edu.academia.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorTest {

    @Test
    void deveCriarAdministradorComLoginESenhaHash() {
        Administrador admin = new Administrador(
                "Admin", LocalDate.of(1990, 1, 1), "admin@email.com", "adminlogin", "hash123");

        assertEquals("Admin", admin.getNome());
        assertEquals("adminlogin", admin.getLogin());
        assertEquals("hash123", admin.getSenhaHash());
    }

    @Test
    void infosDeveConterLogin() {
        Administrador admin = new Administrador(
                "Admin", LocalDate.of(1990, 1, 1), "admin@email.com", "meulogin", "hash");

        assertTrue(admin.infos().contains("meulogin"));
        assertTrue(admin.infos().contains("Administrador"));
    }
}
