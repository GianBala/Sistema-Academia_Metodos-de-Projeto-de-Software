from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Professor import Professor
from typing import List

class GerenciadorProfessores(Gerenciador):
    usuarios: List[Professor] = []
    
    @staticmethod
    def cadastrar_usuario(*professores) -> Professor:
        novo_usuario = Professor(*professores)
        GerenciadorProfessores.usuarios.append(novo_usuario)

        return novo_usuario