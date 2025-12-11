from src.use_cases.Gerenciadores.Gerenciador import Gerenciador
from src.use_cases.Entidades.Aluno import Aluno
from typing import List, Optional

class GerenciadorAlunos(Gerenciador):
    def __init__(self, alunos : Optional[List[Aluno]] = None):
        super().__init__(alunos)

    def cadastrar_usuario(self, *aluno) -> Aluno:
        novo_usuario = Aluno(*aluno)
        self.usuarios.append(novo_usuario)

        return novo_usuario