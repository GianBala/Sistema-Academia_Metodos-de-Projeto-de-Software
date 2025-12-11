import os
from use_cases.Entidades.Aluno import Aluno
from use_cases.Entidades.Funcionario import Funcionario
from use_cases.Entidades.Professor import Professor
from use_cases.Entidades.Atendente import Atendente
from use_cases.Entidades.Administrador import Admnistrador
from src.user_interface.MenuPrincipal import MenuPrincipal

class MenuListarUsuario:
    def menu_listar_usuarios():
        print("=== Listar Usuarios ===")
        print("1. Listar Alunos")
        print("2. Listar Funcionarios")
        print("3. Listar Todos")
        print("4. Voltar ao Menu Principal")
        
        escolha = input("Escolha uma opção: ").strip()
        try:
            if escolha == "1":
                print(Aluno.listar_usuarios())
            elif escolha == "2":
                print(Funcionario.listar_usuarios())
            elif escolha == "3":
                print(Admnistrador.listar_usuarios() + "\n" + Aluno.listar_usuarios() + "\n" + Professor.listar_usuarios() + "\n" + Atendente.listar_usuarios() + "\n")
            elif escolha == "4":
                os.system("cls")
                MenuPrincipal.menu_principal()
                return
            
        except Exception as e:
            print(f"Erro ao listar usuarios: {e}")

        input("\nPressione Enter para continuar...")

        os.system("cls")
        
        MenuPrincipal.menu_principal()