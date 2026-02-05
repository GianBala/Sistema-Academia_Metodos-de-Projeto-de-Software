from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Administrador import Administrador
import src.utils.BancoDeDados.sqlite as sql
from typing import List

class GerenciadorAdministradores(Gerenciador):
    usuarios: List[Administrador] = []

    @staticmethod
    def cadastrar_usuario(nome, dt_nascimento, email, login, senha):
        novo_usuario = Administrador(nome, dt_nascimento, email, login, senha)
        
        #Salvar dados do aluno banco de dados
        db = sql.BancoDeDados()
        db.CadastroAdministrador([nome, dt_nascimento, email, login, senha])

        return novo_usuario

    @staticmethod
    def listarAdministradores():
        #Acessar banco para listar Administrador
        db = sql.BancoDeDados()
        dados = db.Retorna_Todos("administradores")
        print("=" * 25, "Lista de Administradores", "=" * 25)
        for d in dados:
            print(f"Nome:{d[0]:30} Email:{d[2]:30}")