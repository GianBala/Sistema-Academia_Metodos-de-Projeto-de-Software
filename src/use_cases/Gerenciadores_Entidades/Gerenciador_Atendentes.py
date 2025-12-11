from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Atendente import Atendente
from typing import List

class GerenciadorAtendentes(Gerenciador):
    usuarios: List[Atendente] = []
    
    @staticmethod
    def cadastrar_usuario(*atendentes) -> Atendente:
        novo_usuario = Atendente(*atendentes)
        GerenciadorAtendentes.usuarios.append(novo_usuario)

        return novo_usuario