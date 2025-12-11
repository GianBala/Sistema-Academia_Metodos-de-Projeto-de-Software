from use_cases.Entidades.Entidade import Entidade
from datetime import date, datetime
from dateutil.relativedelta import relativedelta

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


    


 