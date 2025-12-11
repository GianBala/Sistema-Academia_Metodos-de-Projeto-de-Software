from src.use_cases.Aluno import Aluno

class Gerenciar_Aluno():
    lista_alunos = []
    
    def __init__(self):
        pass

    def listar_alunos(self) -> str:
        return "\n".join([f"Nome: {aluno.nome}, Data de Nascimento: {aluno.dt_nascimento}, Email: {aluno.email}" for aluno in Gerenciar_Aluno.lista_alunos])    
    
    def cadastrar_aluno(self, nome: str, dt_nascimento: str, email: str):
        novo_aluno = Aluno(nome, dt_nascimento, email)
        return
    
 