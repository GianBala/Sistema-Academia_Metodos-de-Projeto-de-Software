from typing import List, Optional
from src.use_cases.Entidades.Entidade import Entidade

class Gerenciador:
    def __init__(self, usuarios: Optional[List[any]] = None):
        if usuarios is None:
            usuarios = []
        
        self.usuarios = usuarios
    
    def cadastrar_usuario(self, *usuario) -> Entidade:
        novo_usuario = Entidade(*usuario)
        self.usuarios.append(novo_usuario)

        return novo_usuario
    
    def listar_usuarios(self) -> str:
        return "\n".join([usuario.infos() for usuario in self.usuarios])