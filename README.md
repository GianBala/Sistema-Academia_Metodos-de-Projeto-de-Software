<h1 align="center">Sistema Academia - Metodos de Projeto de Software</h1>

<div align="center">
Sistema de gerenciamento de academia com controle de usuarios, turmas, treinos e autenticacao baseada em papeis. Implementa 10 padroes de projeto (GoF).
</div>

<p align="center">&nbsp;</p>

<div align="center">
  <img height="300" src="https://i.pinimg.com/originals/8b/d1/a0/8bd1a07a6beb75a8d742fbc6b3432884.gif" alt="potato head" />
</div>

<h2 align="center">Ferramentas Utilizadas</h2>

<div align="center">
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" alt="java logo" />
<img width="12" />
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/sqlite/sqlite-original.svg" height="40" alt="sqlite logo" />
<img width="12" />
</div>

## Sobre o Projeto

Sistema console de gerenciamento de academia com 4 tipos de usuario, cada um com permissoes e menu proprio:

| Papel | Permissoes |
|-------|-----------|
| **Administrador** | CRUD total de todos os usuarios, turmas, relatorios HTML/PDF, desfazer operacoes, estatisticas |
| **Atendente** | CRUD de alunos e professores, atribuir alunos a turmas |
| **Professor** | Visualizar suas turmas e alunos, criar/atualizar/deletar treinos |
| **Aluno** | Visualizar seus dados e treinos |

### Funcionalidades

- Autenticacao com login/senha (BCrypt) e controle de acesso por papel
- CRUD completo para Alunos, Professores, Atendentes e Administradores
- Gerenciamento de Turmas (professor + alunos) e Treinos (grupo muscular, exercicio, repeticoes, peso)
- Geracao de relatorios HTML e PDF com estatisticas de acesso
- Desfazer ultima operacao (criacao ou atualizacao) via Memento
- Validacao de login (max 12 chars, sem numeros) e senha (politica AWS IAM)
- Admin padrao criado automaticamente na primeira execucao

## Padroes de Projeto Implementados

| # | Padrao | Implementacao |
|---|--------|--------------|
| 1 | **Singleton** | `DatabaseConnection`, `AcademiaFacade` — double-checked locking |
| 2 | **Builder** | `Entidade.BuilderBase` (CRTP), `Turma.Builder`, `Treino.Builder` |
| 3 | **Factory Method** | `RepositoryFactory` → `SqliteRepositoryFactory` |
| 4 | **Template Method** | `Entidade.infos()` (final + hook), `RelatorioTemplate` (HTML/PDF) |
| 5 | **Repository** | Interfaces em `domain/repository/`, implementacoes em `infrastructure/database/` |
| 6 | **Facade** | `AcademiaFacade` — interface unificada com `contarEntidades()` |
| 7 | **Command** | UI commands (`CadastrarAlunoCommand`, etc.) + `FacadeCommand<T>` |
| 8 | **Memento** | `OperacaoMemento` com `Runnable` — desfaz criacao e atualizacao |
| 9 | **Adapter** | `JavaUtilLoggingAdapter` adapta `java.util.logging` para interface `Logger` |
| 10 | **Proxy** | `RepositoryProxy` — controle de acesso por papel (Admin/Atendente/Professor/Aluno) |

## Estrutura do Projeto

```
academia-java/
├── src/main/java/br/edu/academia/
│   ├── App.java                          # Ponto de entrada
│   ├── domain/
│   │   ├── entity/                       # Entidades (Aluno, Professor, Atendente, Administrador, Turma, Treino)
│   │   ├── repository/                   # Interfaces dos repositorios
│   │   ├── service/                      # Servicos (CRUD, autenticacao, sessao)
│   │   ├── facade/                       # AcademiaFacade + FacadeCommand
│   │   ├── memento/                      # HistoricoOperacoes, OperacaoMemento
│   │   ├── report/                       # RelatorioTemplate, HTMLRelatorio, PDFRelatorio
│   │   ├── logging/                      # Interface Logger (Adapter target)
│   │   └── validation/                   # Validadores (login, senha, email, data)
│   ├── infrastructure/
│   │   ├── database/                     # SQLite repos + DatabaseConnection + Factory
│   │   ├── logging/                      # JavaUtilLoggingAdapter
│   │   ├── proxy/                        # RepositoryProxy + proxies especificos
│   │   └── security/                     # BCryptPasswordHasher
│   └── ui/console/                       # Menus (Principal, Login, Admin, Atendente, Professor, Aluno) + Commands
├── src/main/resources/sql/schema.sql     # Schema do banco (8 tabelas)
├── src/test/java/br/edu/academia/        # 220 testes (JUnit 5)
│   ├── testutil/TestStubs.java           # Stubs in-memory compartilhados
│   ├── domain/entity/                    # Testes de entidades
│   ├── domain/memento/                   # Testes de memento
│   ├── domain/service/                   # Testes de servicos (CRUD + autenticacao)
│   ├── domain/validation/                # Testes de validacao
│   ├── infrastructure/database/          # Testes de repositorio SQLite
│   └── pattern/                          # Testes de integridade dos 10 padroes
├── data/                                 # Banco SQLite (criado automaticamente)
└── pom.xml                               # Maven (Java 17, SQLite, jBCrypt, JUnit 5)
```

## Instrucoes para Execucao

### Pre-requisitos

- **Java 17+** instalado
- **Maven 3.9+** instalado (ou use o `mvnw.cmd` incluido no projeto)

### Compilar e executar

```bash
cd academia-java

# Compilar
mvn compile

# Executar
mvn exec:java
```

Ou com o wrapper (Windows):
```cmd
cd academia-java
.\mvnw.cmd exec:java
```

### Rodar os testes

```bash
cd academia-java
mvn test
```

```
Tests run: 220, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Login inicial

Na primeira execucao, um administrador padrao e criado automaticamente:

| Campo | Valor |
|-------|-------|
| Login | `admin` |
| Senha | `Admin@123` |

## Testes

220 testes cobrindo todas as funcionalidades e padroes de projeto:

| Categoria | Testes | Descricao |
|-----------|--------|-----------|
| Servicos (CRUD) | 79 | Cadastrar, atualizar, deletar, buscar, listar + validacoes |
| Padroes de Projeto | 75 | Integridade dos 10 padroes (Singleton, Builder, Factory, Template, Facade, Command, Memento, Adapter, Proxy, Repository) |
| Validacoes | 34 | Login, senha, email, data de nascimento, idade minima |
| Entidades | 8 | Builder, infos(), formatacao |
| Memento | 10 | OperacaoMemento, HistoricoOperacoes, undo de criacao e atualizacao |
| Repositorio SQLite | 8 | CRUD com banco in-memory |
| Autenticacao | 11 | Login por papel, credenciais invalidas, historico |

## Dependencias

| Dependencia | Versao | Uso |
|-------------|--------|-----|
| Java | 17+ | Linguagem |
| SQLite JDBC | 3.45.1.0 | Banco de dados |
| jBCrypt | 0.4 | Hash de senhas |
| JUnit Jupiter | 5.10.2 | Testes |

## Autores

- [Deivison Costa](https://github.com/deivison-costaa)
- [Giancarlo Cavalcante](https://github.com/GianBala)
- [Herlan Lima](https://github.com/herlanlima)
- [Cleydson de Souza](https://github.com/clxxxy)
- [Ivanor Meira](https://github.com/Ivanor-dev)
- [John Victor](https://github.com/johnvictor01)