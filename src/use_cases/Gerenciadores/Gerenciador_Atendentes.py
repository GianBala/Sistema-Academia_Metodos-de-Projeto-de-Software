from src.use_cases.Gerenciadores.Gerenciador import Gerenciador
from src.use_cases.Entidades.Atendente import Atendente
from typing import List, Optional

class GerenciadorAtendentes(Gerenciador):
    def __init__(self, atendentes : Optional[List[Atendente]] = None):
        super().__init__(atendentes)
    
    def cadastrar_usuario(self, *atendentes) -> Atendente:
        novo_usuario = Atendente(*atendentes)
        self.usuarios.append(novo_usuario)

        return novo_usuario