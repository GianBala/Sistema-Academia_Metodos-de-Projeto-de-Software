import sqlite3 as sql

class BancoDeDados:

    def __init__(self):
        # Instanciar objeto com conexão com o banco
        try:
            self.con = sql.connect("data/db.db")
            self.cur = self.con.cursor()
        except:
            print("Erro ao criar se conectar ao Banco")

    def CriacaoBanco(self):
        create_query_path = "src/utils/BancoDeDados/querys/CriacaoBanco.sql"
        with open(create_query_path, "r") as file:
            query = file.read()
            self.cur.executescript(query)
        
        print("Banco Criado com Sucesso!")

    def ChecarseBancoExiste(self):
        query = "SELECT name FROM sqlite_master WHERE type='table' AND name=?"
        self.cur.execute(query, ("alunos",))
        existe = self.cur.fetchone() is not None
        
        if existe == False:
            self.CriacaoBanco()
        else:
            print("Banco incializad com Sucesso!")

    def CadastroAdministrador(self, data, query):
        pass

    def CadastroAluno(self, data):
        query = f"INSERT INTO alunos VALUES ('{data[0]}', '{data[1]}', '{data[2]}', '{data[3]}')"
        self.cur.execute(query)
        self.con.commit()

    def CadastroAtendente(self, data, query):
        pass

    def CadastroProfessor(self, data, query):
        pass

    def Retorna_Todos(self, tabela):
        query = (f"SELECT * FROM {tabela};")
        self.cur.execute(query)
        data = self.cur.fetchall()
        return data