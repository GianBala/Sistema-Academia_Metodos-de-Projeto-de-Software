from src.use_cases.Gerenciadores.Gerenciador import Gerenciador
from src.use_cases.Entidades.Atendente import Atendente
from typing import List, Optional

class GerenciadorAtendentes(Gerenciador):
    def __init__(self, atendentes : Optional[List[Atendente]] = None):
        super().__init__(atendentes)