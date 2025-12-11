from typing import List
from src.use_cases.Entidades.Entidade import Entidade

class Gerenciador:
    usuarios: List[any] = []
    
    @staticmethod
    def cadastrar_usuario(*usuario) -> Entidade:
        novo_usuario = Entidade(*usuario)
        Gerenciador.usuarios.append(novo_usuario)

        return novo_usuario
    
    @classmethod
    def listar_usuarios(cls) -> str:
        return "\n".join([usuario.infos() for usuario in cls.usuarios])