import os
from src.use_cases.Gerenciar_Aluno import Gerenciar_Aluno
from src.user_interface.Menu_Cadastro import Menu_Cadastro

class Menu_Principal:

    @staticmethod
    def run():
        print("=== Menu Principal ===")
        print("1. Cadastrar Usuario")
        print("2. Listar Usuarios")
        print("3. Sair")

        escolha = input("Escolha uma opção: ").strip()

        os.system("cls")

        if escolha == "1":
            Menu_Cadastro.menu_cadastrar_usuario()
        elif escolha == "2":
            print(Gerenciar_Aluno.listar_alunos())
            input("\nPressione Enter para continuar...")

        elif escolha == "3":
            exit()

        else:
            
            print("Opção inválida. Tente novamente.\n")
        
        Menu_Principal.run()
    


    