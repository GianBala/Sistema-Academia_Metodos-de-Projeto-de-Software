from src.use_cases.Gerenciadores_Entidades.Gerenciador import Gerenciador
from src.use_cases.Entidades.Administrador import Administrador
from typing import List

class GerenciadorAdministradores(Gerenciador):
    usuarios: List[Administrador] = []

    @staticmethod
    def cadastrar_usuario(*administrador) -> Administrador:
        novo_usuario = Administrador(*administrador)
        GerenciadorAdministradores.usuarios.append(novo_usuario)

        return novo_usuario