package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.Administrador;
import br.edu.academia.domain.repository.AdministradorRepository;
import br.edu.academia.infrastructure.security.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorServiceTest {

    private AdministradorService service;
    private InMemoryAdminRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAdminRepository();
        PasswordHasher hasher = new PasswordHasher() {
            @Override
            public String hash(String plaintext) {
                return "hashed_" + plaintext;
            }

            @Override
            public boolean verify(String plaintext, String hash) {
                return hash.equals("hashed_" + plaintext);
            }
        };
        service = new AdministradorService(repository, hasher);
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
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Admin", "01/01/1990", "a@e.com", "admin123", "Senha123!"));
    }

    @Test
    void deveRejeitarSenhaFraca() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Admin", "01/01/1990", "a@e.com", "admin", "abc"));
    }

    @Test
    void deveRejeitarLoginMuitoLongo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("Admin", "01/01/1990", "a@e.com",
                        "loginmuitolongo", "Senha123!"));
    }

    static class InMemoryAdminRepository implements AdministradorRepository {
        private final List<Administrador> admins = new ArrayList<>();
        private long nextId = 1;

        @Override
        public void save(Administrador admin) {
            admin.setId(nextId++);
            admins.add(admin);
        }

        @Override
        public List<Administrador> findAll() {
            return new ArrayList<>(admins);
        }

        @Override
        public Optional<Administrador> findById(long id) {
            return admins.stream().filter(a -> a.getId() == id).findFirst();
        }

        @Override
        public Optional<Administrador> findByLogin(String login) {
            return admins.stream().filter(a -> a.getLogin().equals(login)).findFirst();
        }
    }
}
