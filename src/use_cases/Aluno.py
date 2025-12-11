from typing import List, Optional
from src.use_cases.Entidade import Entidade

class Aluno(Entidade):
    lista_registros = []

    def __init__(self, nome: str, dt_nascimento: str, email: str, matricula: Optional[int] = None):
        super().__init__(nome, dt_nascimento, email)
        self.matricula = matricula
        
        Aluno.lista_registros.append(self)
   
class Alunos:
    def __init__(self, alunos : Optional[List[Aluno]] = None):
        
        if alunos is None:
            alunos = []
        
        self.alunos = alunos

    def adicionar_aluno(self, aluno: Aluno):
        self.alunos.append(aluno)

    def listar_alunos(self) -> str:
        return "\n".join([aluno.infos() for aluno in self.alunos])    