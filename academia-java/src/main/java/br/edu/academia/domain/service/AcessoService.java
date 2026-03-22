package br.edu.academia.domain.service;

import br.edu.academia.domain.entity.RegistroAcesso;
import br.edu.academia.domain.repository.RegistroAcessoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AcessoService {

    private final RegistroAcessoRepository repository;

    public AcessoService(RegistroAcessoRepository repository) {
        this.repository = repository;
    }

    public void registrarOperacao(String tipoOperacao, String tipoEntidade) {
        RegistroAcesso registro = new RegistroAcesso(tipoOperacao, tipoEntidade, LocalDateTime.now());
        repository.save(registro);
    }

    public List<RegistroAcesso> listarTodos() {
        return repository.findAll();
    }

    public long contarPorOperacaoEEntidade(String tipoOperacao, String tipoEntidade) {
        return repository.countByTipoOperacaoAndTipoEntidade(tipoOperacao, tipoEntidade);
    }
}
