import os
from use_cases.Gerenciar_Aluno import Gerenciar_Aluno
from Menu_Cadastro import Menu_Cadastro

class Menu_Principal:

    def __init__(self):
        pass

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
            Gerenciar_Aluno.listar_alunos()
            input("\nPressione Enter para continuar...")

        elif escolha == "3":
            exit()

        else:
            
            print("Opção inválida. Tente novamente.\n")
            Menu_Principal.run()
    


    