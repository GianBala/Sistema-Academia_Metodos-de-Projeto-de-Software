from use_cases.Entidades.Entidade import Entidade

class Administrador(Entidade):

    def __init__(self, nome: str, dt_nascimento: str, email: str, senha : str):
        super().__init__(nome, dt_nascimento, email)
        self.senha = senha

    @property
    def senha(self) -> str:
        return self._senha
    
    @senha.setter
    def senha(self, senha: str):
        if len(senha) < 6:
            raise ValueError("A senha deve ter pelo menos 6 caracteres.")
        else:
            self._senha = senha