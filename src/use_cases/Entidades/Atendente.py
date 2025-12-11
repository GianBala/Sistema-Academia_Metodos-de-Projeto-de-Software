from use_cases.Entidades.Funcionario import Funcionario
from use_cases.Entidades.Aluno import Aluno
from typing import List, Optional

class Atendente(Funcionario):
    lista_registros = []

    def __init__(self, nome: str, idade: int, email: str):
        super().__init__(nome, idade, email)
        
        Atendente.lista_registros.append(self)