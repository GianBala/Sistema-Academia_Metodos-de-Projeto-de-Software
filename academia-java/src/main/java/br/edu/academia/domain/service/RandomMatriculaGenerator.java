package br.edu.academia.domain.service;

import br.edu.academia.domain.repository.AlunoRepository;

import java.util.Random;

public class RandomMatriculaGenerator implements MatriculaGenerator {

    private final AlunoRepository repository;
    private final Random random;

    public RandomMatriculaGenerator(AlunoRepository repository) {
        this.repository = repository;
        this.random = new Random();
    }

    @Override
    public int generate() {
        int matricula;
        do {
            matricula = random.nextInt(9999) + 1;
        } while (repository.findByMatricula(matricula).isPresent());
        return matricula;
    }
}
