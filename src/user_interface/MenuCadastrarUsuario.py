from use_cases.Entidades.Aluno import Aluno
from use_cases.Entidades.Funcionario import Funcionario
from use_cases.Entidades.Professor import Professor
from use_cases.Entidades.Atendente import Atendente
from use_cases.Entidades.Administrador import Admnistrador
from src.user_interface.MenuPrincipal import MenuPrincipal
import os

class MenuCadastrarUsuario:
    def menu_cadastrar_usuario():
        print("=== Cadastrar Usuario ===")
        tipo = input("Tipo de Usuario (1-Administrador, 2-Aluno, 3-Funcionario): ")
        nome = input("Nome: ")
        dt_nascimento = input("Data de Nascimento: ")
        email = input("Email: ")
        
        try:

            if tipo == "1":
                senha = input("Senha: ").strip()
                novo_usuario = Admnistrador(nome, dt_nascimento, email, senha)

            elif tipo == "2":
                novo_usuario = Aluno(nome, dt_nascimento, email, matricula=1)

            elif tipo == "3":
                tipo_funcionario = input("Tipo de Funcionario (1-Professor, 2-Atendente): ")
                
                if tipo_funcionario == "1":
                    novo_usuario = Professor(nome, dt_nascimento, email)

                elif tipo_funcionario == "2":
                    novo_usuario = Atendente(nome, dt_nascimento, email)
            
            else:
                print("Tipo de usuario inválido.")
                MenuPrincipal.menu_principal()
                return
            
            print(f"Usuario {novo_usuario.nome} cadastrado com sucesso!")
        
        except Exception as e:
            print(f"Erro ao cadastrar usuario: {e}")
        
        input("\nPressione Enter para continuar...")

        os.system("cls")

        MenuPrincipal.menu_principal()

        return