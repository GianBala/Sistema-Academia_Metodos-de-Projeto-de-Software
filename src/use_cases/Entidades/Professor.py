from use_cases.Entidades.Funcionario import Funcionario
from use_cases.Entidades.Aluno import Aluno
from typing import List, Optional

class Professor(Funcionario):

    def __init__(self, nome: str, idade: int, email: str, alunos : Optional[List[Aluno]] = None):
        super().__init__(nome, idade, email)
        
        if alunos is None:
            alunos = []

        self.alunos = alunos

    def adicionar_aluno(self, aluno: Aluno):
        self.alunos.append(aluno)

    def listar_alunos(self) -> str:
        return "\n".join([aluno.infos() for aluno in self.alunos])