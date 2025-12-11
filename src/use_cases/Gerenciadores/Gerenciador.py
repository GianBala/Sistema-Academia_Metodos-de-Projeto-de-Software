from typing import List, Optional

class Gerenciador:
    def __init__(self, usuarios: Optional[List[any]] = None):
        if usuarios is None:
            usuarios = []
        
        self.usuarios = usuarios
    
    def cadastrar_usuario(self, usuario):
        self.usuarios.append(usuario)
    
    def listar_usuarios(self) -> str:
        return "\n".join([usuario.infos() for usuario in self.usuarios])