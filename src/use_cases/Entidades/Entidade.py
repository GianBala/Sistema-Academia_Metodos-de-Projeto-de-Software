from datetime import datetime, date
from dateutil.relativedelta import relativedelta

class Entidade:
    lista_registros = []
    
    def __init__(self, nome: str, dt_nascimento: str, email: str):
        self.id = 0
        self._nome = nome.strip().title()
        self.dt_nascimento = dt_nascimento
        self.email = email

        Entidade.lista_registros.append(self)

    @property
    def nome(self) -> str:
        return self._nome

    @property
    def dt_nascimento(self) -> datetime:
        return self._dt_nascimento
    
    @dt_nascimento.setter
    def dt_nascimento(self, data):
        
        try:
            # Formato com barras (dd/mm/aaaa)
            data = datetime.strptime(data, '%d/%m/%Y').date()
        except ValueError:
            try:
                # Formato ISO (aaaa-mm-dd)
                data = datetime.strptime(data, '%Y-%m-%d').date()
            except ValueError:
                raise ValueError(f"Formato de data inválido: '{data}'. Use 'dd/mm/aaaa' ou 'aaaa-mm-dd'.")

        hoje = date.today()
        
        if data > hoje:
            raise ValueError("Data de nascimento não pode ser no futuro.")
        else:
            self._dt_nascimento = data

    @property
    def idade(self) -> int:
        """Calcula a idade atual do funcionário."""
        hoje = date.today()
    
        diferenca = relativedelta(hoje, self._dt_nascimento)
        return diferenca.years
    
  
    @property
    def email(self) -> str:
        return self._email
    
    @email.setter
    def email(self, mail: str):
        if "@" not in mail or "." not in mail:
            raise ValueError(f"O email {mail} é inválido, tente outro email.")
        else:
            self._email = mail
    

    def infos(self) -> str:
        return f"Nome: {self.nome}, Idade: {self.idade}, Email: {self.email}, Cargo: {self.__class__.__name__}"
    
    @classmethod
    def listar_usuarios(cls) -> str:
        return "\n".join([funcionario.infos() for funcionario in cls.lista_registros]) 