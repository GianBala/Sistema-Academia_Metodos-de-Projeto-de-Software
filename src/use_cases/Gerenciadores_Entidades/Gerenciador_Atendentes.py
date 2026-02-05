from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Atendente import Atendente
import src.utils.BancoDeDados.sqlite as sql
from typing import List

class GerenciadorAtendentes(Gerenciador):
    usuarios: List[Atendente] = []
    
    @staticmethod
    def cadastrar_usuario(nome, dt_nascimento, email):
        novo_usuario = Atendente(nome, dt_nascimento, email)
        
        #Salvar dados do aluno banco de dados
        db = sql.BancoDeDados()
        db.CadastroAtendente([nome, dt_nascimento, email])

        return novo_usuario

    @staticmethod
    def listarAtendentes():
        #Acessar banco para listar Atendente
        db = sql.BancoDeDados()
        dados = db.Retorna_Todos("atendentes")
        print("=" * 27, "Lista de Atendentes", "=" * 27)
        for d in dados:
            print(f"Nome:{d[0]:30} Email:{d[2]:30}")

    