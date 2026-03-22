package br.edu.academia.domain.entity;

import java.time.LocalDate;

public class AlunoMemento {

    private final long id;
    private final String nome;
    private final LocalDate dataNascimento;
    private final String email;
    private final int matricula;

    public AlunoMemento(long id, String nome, LocalDate dataNascimento, String email, int matricula) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.matricula = matricula;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public int getMatricula() {
        return matricula;
    }
}
