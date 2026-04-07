package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.Treino;

import java.util.List;

public interface TreinoRepository extends Repository<Treino> {
    List<Treino> findByAlunoId(long alunoId);
}
