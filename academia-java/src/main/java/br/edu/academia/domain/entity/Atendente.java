package br.edu.academia.domain.entity;

import java.time.LocalDate;

public class Atendente extends Funcionario {

    public Atendente(String nome, LocalDate dataNascimento, String email) {
        super(nome, dataNascimento, email);
    }
}
