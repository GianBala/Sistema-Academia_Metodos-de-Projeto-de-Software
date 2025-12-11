from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Aluno import Aluno
from typing import List

class GerenciadorAlunos(Gerenciador):
    usuarios: List[Aluno] = []

    @staticmethod
    def cadastrar_usuario(*aluno) -> Aluno:
        novo_usuario = Aluno(*aluno)
        GerenciadorAlunos.usuarios.append(novo_usuario)

        return novo_usuario