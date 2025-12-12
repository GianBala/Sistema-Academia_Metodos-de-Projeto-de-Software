from src.use_cases.Gerenciar_Aluno import Gerenciar_Aluno
import os

class Menu_Cadastro:
    
    @staticmethod
    def menu_cadastrar_usuario():
        print("=== Cadastrar Usuario ===")
        nome = input("Nome: ")
        dt_nascimento = input("Data de Nascimento: ")
        email = input("Email: ")
        
        Gerenciar_Aluno.cadastrar_aluno(nome, dt_nascimento, email)
        
        print("Aluno cadastrado com sucesso!")
        
        input("\nPressione Enter para continuar...")

        os.system("cls")

        from src.user_interface.Menu_Principal import Menu_Principal
        Menu_Principal.run()

        return