package br.edu.academia.domain.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import br.edu.academia.domain.entity.Entidade;
import br.edu.academia.infrastructure.database.*;
import br.edu.academia.domain.entity.*;


public class RelatorioService {

    private String titulo;
    private LocalDateTime dataGeracao;

    private final SqliteAlunoRepository     alunoRepo;
    private final SqliteProfessorRepository professorRepo;
    private final SqliteAtendenteRepository atendenteRepo;
    private final SqliteAdministradorRepository adminRepo;

    public RelatorioService(String titulo,
                     SqliteAlunoRepository alunoRepo,
                     SqliteProfessorRepository professorRepo,
                     SqliteAtendenteRepository atendenteRepo,
                     SqliteAdministradorRepository adminRepo) {
        this.titulo = titulo;
        this.alunoRepo = alunoRepo;
        this.professorRepo = professorRepo;
        this.atendenteRepo = atendenteRepo;
        this.adminRepo = adminRepo;
        this.dataGeracao = LocalDateTime.now();
    }

    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();

        sb.append("===== ").append(titulo).append(" =====\n");
        sb.append("Gerado em: ")
          .append(dataGeracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
          .append("\n\n");
        
        List<Aluno> alunos = new ArrayList<>();
        List<Administrador> adms = new ArrayList<>();
        List<Professor> profs = new ArrayList<>();
        List<Atendente> atendentes = new ArrayList<>();

        alunos = alunoRepo.findAll();
        adms = adminRepo.findAll();
        profs = professorRepo.findAll();
        atendentes = atendenteRepo.findAll();
        
        sb.append("\nTotal de Alunos:").append(alunos.size());
        sb.append("\nTotal de Atendentes:").append(atendentes.size());
        sb.append("\nTotal de Professores:").append(profs.size());
        sb.append("\nTotal de Adminstradores:").append(adms.size());
    
        return sb.toString();
    }
}