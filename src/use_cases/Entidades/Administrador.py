from src.use_cases.Entidades.Entidade import Entidade
from src.utils.Tratamentos.Tratamento_Senha import TratamentoSenha
from src.utils.Tratamentos.Tratamento_Login import TratamentoLogin

class Administrador(Entidade):

    def __init__(self, nome: str, dt_nascimento: str, email: str, login: str, senha : str):
        super().__init__(nome, dt_nascimento, email)
        self.login = login
        self.senha = senha

    @property
    def senha(self) -> str:
        return self._senha
    
    @senha.setter
    def senha(self, senha: str):
        if TratamentoSenha.verificar_senha(senha, self):
            self._senha = senha 

    @property
    def login(self) -> str:
        return self._login
    
    @login.setter
    def login(self, login : str):
        login = login.strip()

        if TratamentoLogin.verificar_login(login):
            self._login = login
        