from src.use_cases.Aluno import Aluno
from src.use_cases.Entidade import Entidade
from typing import List, Optional
from datetime import date, datetime
from dateutil.relativedelta import relativedelta

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

class Funcionario(Entidade):
    lista_registros = []

    def __init__(self, nome: str, dt_nascimento: str, email: str):
        super().__init__(nome, dt_nascimento,email)

        Funcionario.lista_registros.append(self)

    @Entidade.dt_nascimento.setter
    def dt_nascimento(self, data: str):
        
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

        diferenca = relativedelta(hoje, data)
        
        if diferenca.years < 18:
            raise ValueError("Funcionários devem ter pelo menos 18 anos.")
        else:
            self._dt_nascimento = data

class Professor(Funcionario):
    lista_registros = []

    def __init__(self, nome: str, idade: int, email: str, alunos : Optional[List[Aluno]] = None):
        super().__init__(nome, idade, email)
        
        if alunos is None:
            alunos = []

        self.alunos = alunos
        
        Professor.lista_registros.append(self)

    def adicionar_aluno(self, aluno: Aluno):
        self.alunos.append(aluno)

    def listar_alunos(self) -> str:
        return "\n".join([aluno.infos() for aluno in self.alunos])
    

class Atendente(Funcionario):
    lista_registros = []

    def __init__(self, nome: str, idade: int, email: str):
        super().__init__(nome, idade, email)
        
        Atendente.lista_registros.append(self)
 