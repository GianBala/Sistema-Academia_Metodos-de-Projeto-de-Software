package br.edu.academia.domain.entity;

import java.time.LocalDate;

public class Aluno extends Entidade {

    private final int matricula;

    public Aluno(String nome, LocalDate dataNascimento, String email, int matricula) {
        super(nome, dataNascimento, email);
        this.matricula = matricula;
    }

    public int getMatricula() {
        return matricula;
    }

    @Override
    public String infos() {
        return super.infos() + String.format(" | Matricula: %d", matricula);
    }
}
