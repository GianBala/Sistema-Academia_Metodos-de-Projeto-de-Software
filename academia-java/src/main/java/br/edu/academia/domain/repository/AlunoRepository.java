package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.Aluno;

import java.util.Optional;

public interface AlunoRepository extends Repository<Aluno> {
    Optional<Aluno> findByMatricula(int matricula);
}
