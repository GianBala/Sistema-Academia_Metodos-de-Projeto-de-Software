from typing import List, Optional
from src.use_cases.Entidade import Entidade

class Aluno(Entidade):
    lista_registros = []

    def __init__(self, nome: str, dt_nascimento: str, email: str, matricula: Optional[int] = None):
        super().__init__(nome, dt_nascimento, email)
        self.matricula = matricula
        
        Aluno.lista_registros.append(self)
