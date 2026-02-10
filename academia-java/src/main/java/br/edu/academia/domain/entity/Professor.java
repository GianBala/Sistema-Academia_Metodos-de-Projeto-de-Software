package br.edu.academia.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Professor extends Funcionario {

    private final List<Aluno> alunos;

    public Professor(String nome, LocalDate dataNascimento, String email) {
        super(nome, dataNascimento, email);
        this.alunos = new ArrayList<>();
    }

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public List<Aluno> getAlunos() {
        return Collections.unmodifiableList(alunos);
    }
}
