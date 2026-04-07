package br.edu.academia.domain.repository;

import br.edu.academia.domain.entity.Turma;

import java.util.List;

public interface TurmaRepository extends Repository<Turma> {
    List<Turma> findByProfessorId(long professorId);
    void addAluno(long turmaId, long alunoId);
    void removeAluno(long turmaId, long alunoId);
    List<Long> findAlunoIds(long turmaId);
}
