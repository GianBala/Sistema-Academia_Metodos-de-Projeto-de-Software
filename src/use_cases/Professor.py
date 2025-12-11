from src.use_cases.Funcionario import Funcionario
from src.use_cases.Aluno import Aluno
from typing import List, Optional

class Professor(Funcionario):
    lista_registros = []

    def __init__(self, nome: str, idade: int, email: str, alunos : Optional[List[Aluno]] = None):
        super().__init__(nome, idade, email)
        
        if alunos is None:
            alunos = []

        self.alunos = alunos
        
        Professor.lista_registros.append(self)

    def adicionar_aluno(self, aluno: Aluno):
        self.alunos.append(aluno)

    def listar_alunos(self) -> str:
        return "\n".join([aluno.infos() for aluno in self.alunos])