package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.Administrador;

import java.util.Optional;

public interface AdministradorRepository extends Repository<Administrador> {
    Optional<Administrador> findByLogin(String login);
}
