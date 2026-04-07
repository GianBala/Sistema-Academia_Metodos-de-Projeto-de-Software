package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.Atendente;

import java.util.Optional;

public interface AtendenteRepository extends Repository<Atendente> {
    Optional<Atendente> findByLogin(String login);
}
