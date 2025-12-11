from src.use_cases.Gerenciadores.Gerenciador import Gerenciador
from src.use_cases.Entidades.Administrador import Administrador
from typing import List, Optional

class GerenciadorAdministradores(Gerenciador):
    def __init__(self, administradores : Optional[List[Administrador]] = None):
        super().__init__(administradores)

    def cadastrar_usuario(self, *administrador) -> Administrador:
        novo_usuario = Administrador(*administrador)
        self.usuarios.append(novo_usuario)

        return novo_usuario