from src.use_cases.Gerenciadores.Gerenciador import Gerenciador
from src.use_cases.Entidades.Professor import Professor
from typing import List, Optional

class GerenciadorProfessores(Gerenciador):
    def __init__(self, professores : Optional[List[Professor]] = None):
        super().__init__(professores)
    
    def cadastrar_usuario(self, *professores) -> Professor:
        novo_usuario = Professor(*professores)
        self.usuarios.append(novo_usuario)

        return novo_usuario