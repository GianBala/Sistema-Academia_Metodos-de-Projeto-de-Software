import os
from src.use_cases.Aluno import Aluno
from src.use_cases.Funcionario import Funcionario, Professor, Atendente, Admnistrador
from src.use_cases.Entidade import Entidade

def menu_principal():
    print("=== Menu Principal ===")
    print("1. Cadastrar Usuario")
    print("2. Listar Usuarios")
    print("3. Sair")

    escolha = input("Escolha uma opção: ").strip()

    os.system("cls")

    if escolha == "1":
        menu_cadastrar_usuario()
    elif escolha == "2":
        menu_listar_usuarios()

    elif escolha == "3":
        exit()

    else:
        print("Opção inválida. Tente novamente.\n")
        menu_principal()
    

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
            menu_principal()
            return
        
        print(f"Usuario {novo_usuario.nome} cadastrado com sucesso!")
    
    except Exception as e:
        print(f"Erro ao cadastrar usuario: {e}")
    
    input("\nPressione Enter para continuar...")

    os.system("cls")

    menu_principal()

    return
    
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
            menu_principal()
            return
        
    except Exception as e:
        print(f"Erro ao listar usuarios: {e}")

    input("\nPressione Enter para continuar...")

    os.system("cls")
    
    menu_principal()