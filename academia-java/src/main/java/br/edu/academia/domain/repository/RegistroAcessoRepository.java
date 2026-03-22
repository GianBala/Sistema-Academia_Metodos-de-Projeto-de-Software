package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.RegistroAcesso;

import java.util.List;

public interface RegistroAcessoRepository extends Repository<RegistroAcesso> {

    List<RegistroAcesso> findByTipoOperacao(String tipoOperacao);

    List<RegistroAcesso> findByTipoEntidade(String tipoEntidade);

    long countByTipoOperacaoAndTipoEntidade(String tipoOperacao, String tipoEntidade);
}
