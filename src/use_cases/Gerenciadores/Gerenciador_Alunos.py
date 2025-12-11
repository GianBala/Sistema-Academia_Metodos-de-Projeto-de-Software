from src.use_cases.Gerenciadores.Gerenciador import Gerenciador

class GerenciadorProfessores(Gerenciador):
    def __init__(self, professores=None):
        super().__init__(professores)