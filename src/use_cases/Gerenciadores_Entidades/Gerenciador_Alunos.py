from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Aluno import Aluno
import src.utils.BancoDeDados.sqlite as sql
from typing import List
from random import randint

class GerenciadorAlunos(Gerenciador):
    usuarios: List[Aluno] = []

    @staticmethod
    def cadastrar_usuario(nome, dt_nascimento, email) -> Aluno:
        matricula = randint(1, 9999)
        novo_usuario = Aluno(nome, dt_nascimento, email, matricula)
        
        #Salvar dados do aluno banco de dados
        db = sql.BancoDeDados()
        db.CadastroAluno([nome, dt_nascimento, email, matricula])


        return novo_usuario

    @staticmethod
    def listarAlunos():
        #Acessar banco para listar alunos
        db = sql.BancoDeDados()
        dados = db.Retorna_Todos("alunos")
        for d in dados:
            print(f"Nome:{d[0]:30} Email:{d[2]:30} Matricula:{d[3]}")