package br.edu.academia.domain.entity;

import java.time.LocalDate;

public abstract class Funcionario extends Entidade {

    protected Funcionario(String nome, LocalDate dataNascimento, String email) {
        super(nome, dataNascimento, email);
    }
}
