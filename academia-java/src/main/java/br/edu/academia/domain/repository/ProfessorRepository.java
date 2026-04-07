package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.Professor;

import java.util.Optional;

public interface ProfessorRepository extends Repository<Professor> {
    Optional<Professor> findByLogin(String login);
}
