from src.use_cases.Aluno import Aluno
from typing import List

class Gerenciar_Aluno:
    lista_alunos :  List[Aluno] = []  

    @staticmethod
    def listar_alunos() -> str:
        return "\n".join([f"Nome: {aluno._nome}, Data de Nascimento: {aluno.dt_nascimento}, Email: {aluno.email}" for aluno in Gerenciar_Aluno.lista_alunos])    
    
    @staticmethod
    def cadastrar_aluno(nome: str, dt_nascimento: str, email: str):
        novo_aluno = Aluno(nome, dt_nascimento, email)
        Gerenciar_Aluno.lista_alunos.append(novo_aluno)
        return
    
 