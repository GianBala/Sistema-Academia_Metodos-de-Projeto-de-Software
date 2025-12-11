from src.use_cases.Entidade import Entidade

class Admnistrador(Entidade):
    lista_registros = []

    def __init__(self, nome: str, dt_nascimento: str, email: str, senha : str):
        super().__init__(nome, dt_nascimento, email)
        self.senha = senha
        
        Admnistrador.lista_registros.append(self)

    @property
    def senha(self) -> str:
        return self._senha
    
    @senha.setter
    def senha(self, senha: str):
        if len(senha) < 6:
            raise ValueError("A senha deve ter pelo menos 6 caracteres.")
        else:
            self._senha = senha