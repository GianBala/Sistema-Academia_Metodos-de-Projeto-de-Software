import os
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Administradores import GerenciadorAdministradores
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Alunos import GerenciadorAlunos
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Atendentes import GerenciadorAtendentes
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Professores import GerenciadorProfessores

class MenuListarUsuario:
    
    @staticmethod    
    def menu_listar_usuarios():
        print("=== Listar Usuarios ===")
        print("1. Listar Alunos")
        print("2. Listar Todos")
        print("3. Voltar ao Menu Principal")
        from src.user_interface.Menu_Principal import MenuPrincipal
        escolha = input("Escolha uma opção: ").strip()
        try:
            if escolha == "1":
                print(GerenciadorAlunos.listar_usuarios())
            elif escolha == "2":
                print(GerenciadorAdministradores.listar_usuarios() + "\n" + GerenciadorAlunos.listar_usuarios() + "\n" + GerenciadorProfessores.listar_usuarios() + "\n" + GerenciadorAtendentes.listar_usuarios() + "\n")
            elif escolha == "3":
                os.system("cls")
                
                MenuPrincipal.menu_principal()
                return
            
        except Exception as e:
            print(f"Erro ao listar usuarios: {e}")

        input("\nPressione Enter para continuar...")

        os.system("cls")
        
        MenuPrincipal.menu_principal()