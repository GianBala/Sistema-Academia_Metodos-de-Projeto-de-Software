package br.edu.academia.domain.entity;

import java.time.LocalDate;

public class Aluno extends Entidade {

    private int matricula;

    public Aluno(String nome, LocalDate dataNascimento, String email, int matricula) {
        super(nome, dataNascimento, email);
        this.matricula = matricula;
    }

    public int getMatricula() {
        return matricula;
    }

    public AlunoMemento criarMemento() {
        return new AlunoMemento(getId(), getNome(), getDataNascimento(), getEmail(), matricula);
    }

    public void restaurarMemento(AlunoMemento memento) {
        setNome(memento.getNome());
        setDataNascimento(memento.getDataNascimento());
        setEmail(memento.getEmail());
        this.matricula = memento.getMatricula();
    }

    @Override
    public String infos() {
        return super.infos() + String.format(" | Matricula: %d", matricula);
    }
}
