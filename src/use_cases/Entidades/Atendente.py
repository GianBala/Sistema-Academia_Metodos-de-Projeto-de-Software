from use_cases.Entidades.Funcionario import Funcionario

class Atendente(Funcionario):

    def __init__(self, nome: str, idade: int, email: str):
        super().__init__(nome, idade, email)
        