from typing import Optional
from use_cases.Entidades.Entidade import Entidade

class Aluno(Entidade):

    def __init__(self, nome: str, dt_nascimento: str, email: str, matricula: Optional[int] = None):
        super().__init__(nome, dt_nascimento, email)
        self.matricula = matricula
        
