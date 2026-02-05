import os
from src.user_interface.Menu_Cadastrar_Usuario import MenuCadastrarUsuario
from src.user_interface.Menu_Listar_Usuario import MenuListarUsuario
from src.utils.Interface.visual import clean_console

class MenuPrincipal:
    
    @staticmethod
    def menu_principal():
        
        #Limpar o console (NÃO DA PARA TRABALHAR COM POLUIÇÃO VISUAL)
        clean_console()

        print("=== Menu Principal ===")
        print("1. Cadastrar Usuario")
        print("2. Listar Usuarios")
        print("3. Sair")

        escolha = input("Escolha uma opção: ").strip()

        os.system("cls")

        if escolha == "1":
            MenuCadastrarUsuario.menu_cadastrar_usuario()
        elif escolha == "2":
            MenuListarUsuario.menu_listar_usuarios()

        elif escolha == "3":
            exit()

        else:
            print("Opção inválida. Tente novamente.\n")
            MenuPrincipal.menu_principal()