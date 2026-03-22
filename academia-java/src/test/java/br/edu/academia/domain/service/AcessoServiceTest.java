package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.RegistroAcesso;
import br.edu.academia.domain.repository.RegistroAcessoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AcessoServiceTest {

    private AcessoService service;

    @BeforeEach
    void setUp() {
        service = new AcessoService(new InMemoryRegistroAcessoRepository());
    }

    @Test
    void deveRegistrarOperacao() {
        service.registrarOperacao("CADASTRO", "ALUNO");
        List<RegistroAcesso> todos = service.listarTodos();
        assertEquals(1, todos.size());
        assertEquals("CADASTRO", todos.get(0).getTipoOperacao());
        assertEquals("ALUNO", todos.get(0).getTipoEntidade());
    }

    @Test
    void deveContarPorOperacaoEEntidade() {
        service.registrarOperacao("CADASTRO", "ALUNO");
        service.registrarOperacao("CADASTRO", "ALUNO");
        service.registrarOperacao("LISTAGEM", "ALUNO");
        service.registrarOperacao("CADASTRO", "PROFESSOR");

        assertEquals(2, service.contarPorOperacaoEEntidade("CADASTRO", "ALUNO"));
        assertEquals(1, service.contarPorOperacaoEEntidade("LISTAGEM", "ALUNO"));
        assertEquals(1, service.contarPorOperacaoEEntidade("CADASTRO", "PROFESSOR"));
    }

    @Test
    void deveRetornarZeroSemRegistros() {
        assertEquals(0, service.contarPorOperacaoEEntidade("CADASTRO", "ALUNO"));
    }

    @Test
    void deveListarTodosRegistros() {
        service.registrarOperacao("CADASTRO", "ALUNO");
        service.registrarOperacao("LISTAGEM", "PROFESSOR");
        assertEquals(2, service.listarTodos().size());
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
