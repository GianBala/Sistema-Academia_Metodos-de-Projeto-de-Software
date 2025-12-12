from typing import List, Optional

class Aluno:
    lista_registros = []

    def __init__(self, nome: str, dt_nascimento: str, email: str, matricula: Optional[int] = None):
        self._nome = nome.strip().title()
        self.dt_nascimento = dt_nascimento
        self.email = email
        self.matricula = matricula
        

