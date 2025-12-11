import os
from src.use_cases.Aluno import Aluno
from src.use_cases.Funcionario import Funcionario
from src.use_cases.Professor import Professor
from src.use_cases.Atendente import Atendente
from src.use_cases.Administrador import Admnistrador
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
                print(Aluno.listar_funcionarios())
            elif escolha == "2":
                print(Funcionario.listar_funcionarios())
            elif escolha == "3":
                print(Admnistrador.listar_funcionarios() + "\n" + Aluno.listar_funcionarios() + "\n" + Professor.listar_funcionarios() + "\n" + Atendente.listar_funcionarios() + "\n")
            elif escolha == "4":
                os.system("cls")
                MenuPrincipal.menu_principal()
                return
            
        except Exception as e:
            print(f"Erro ao listar usuarios: {e}")

        input("\nPressione Enter para continuar...")

        os.system("cls")
        
        MenuPrincipal.menu_principal()