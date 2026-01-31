from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Professor import Professor
import src.utils.BancoDeDados.sqlite as sql
from typing import List

class GerenciadorProfessores(Gerenciador):
    usuarios: List[Professor] = []
    
    @staticmethod
    def cadastrar_usuario(nome, dt_nascimento, email):
        novo_usuario = Professor(nome, dt_nascimento, email)
        
        #Salvar dados do aluno banco de dados
        db = sql.BancoDeDados()
        db.CadastroProfessor([nome, dt_nascimento, email])

        return novo_usuario

    @staticmethod
    def listarProfessores():
        #Acessar banco para listar Professor
        db = sql.BancoDeDados()
        dados = db.Retorna_Todos("professores")
        print("=" * 27, "Lista de Professores", "=" * 27)
        for d in dados:
            print(f"Nome:{d[0]:30} Email:{d[2]:30}")