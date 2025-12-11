from src.use_cases.Gerenciadores_Entidades.Gerenciador_Administradores import GerenciadorAdministradores
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Alunos import GerenciadorAlunos
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Atendentes import GerenciadorAtendentes
from src.use_cases.Gerenciadores_Entidades.Gerenciador_Professores import GerenciadorProfessores
import os

class MenuCadastrarUsuario:
    
    @staticmethod
    def menu_cadastrar_usuario():
        print("=== Cadastrar Usuario ===")
        tipo = input("Tipo de Usuario (1-Administrador, 2-Aluno, 3-Funcionario): ")
        nome = input("Nome: ")
        dt_nascimento = input("Data de Nascimento: ")
        email = input("Email: ")
        from src.user_interface.Menu_Principal import MenuPrincipal
        try:
            novo_usuario = None
            
            if tipo == "1":
                login = input("Login: ").strip()
                senha = input("Senha: ").strip()
                
                novo_usuario = GerenciadorAdministradores.cadastrar_usuario(nome, dt_nascimento, email, login, senha)

            elif tipo == "2":
                novo_usuario = GerenciadorAlunos.cadastrar_usuario(nome, dt_nascimento, email, matricula=1)

            elif tipo == "3":
                tipo_funcionario = input("Tipo de Funcionario (1-Professor, 2-Atendente): ")
                
                if tipo_funcionario == "1":
                    novo_usuario = GerenciadorProfessores.cadastrar_usuario(nome, dt_nascimento, email)

                elif tipo_funcionario == "2":
                    novo_usuario = GerenciadorAtendentes.cadastrar_usuario(nome, dt_nascimento, email)
            
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