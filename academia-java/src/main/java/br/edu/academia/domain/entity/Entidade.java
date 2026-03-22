package br.edu.academia.domain.entity;

import java.time.LocalDate;
import java.time.Period;

public abstract class Entidade {

    private long id;
    private String nome;
    private LocalDate dataNascimento;
    private String email;

    protected Entidade(String nome, LocalDate dataNascimento, String email) {
        this.nome = formatarNome(nome);
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = formatarNome(nome);
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public String infos() {
        return String.format("Nome: %s | Idade: %d | Email: %s | Cargo: %s",
                nome, getIdade(), email, getCargo());
    }

    protected String getCargo() {
        return getClass().getSimpleName();
    }

    protected String formatarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return nome;
        }
        String[] palavras = nome.trim().toLowerCase().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palavra : palavras) {
            if (!resultado.isEmpty()) {
                resultado.append(" ");
            }
            resultado.append(Character.toUpperCase(palavra.charAt(0)));
            resultado.append(palavra.substring(1));
        }
        return resultado.toString();
    }
}
