from src.user_interface.Menu_Principal import MenuPrincipal
import src.utils.BancoDeDados.sqlite as sql

# Conexão com banco de Dados
db = sql.BancoDeDados()
db.ChecarseBancoExiste()

# Chama parte principal da Aplicação
MenuPrincipal.menu_principal()