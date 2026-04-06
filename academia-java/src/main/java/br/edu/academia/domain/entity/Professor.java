package br.edu.academia.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Professor extends Funcionario {

    private final List<Aluno> alunos;

    private Professor(Builder builder) {
        super(builder);
        this.alunos = new ArrayList<>();
    }

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public List<Aluno> getAlunos() {
        return Collections.unmodifiableList(alunos);
    }

    public static class Builder extends BuilderBase<Builder> {
        @Override
        public Professor build() {
            validarCamposBase();
            return new Professor(this);
        }
    }
}