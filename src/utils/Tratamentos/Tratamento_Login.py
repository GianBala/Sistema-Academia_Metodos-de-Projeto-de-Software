class TratamentoLogin:
    @staticmethod
    def verificar_login(login : str):
        if login == "":
            raise ValueError("O login não pode ser vazio")
        elif any(char.isdigit() for char in login):
            raise ValueError("O login não pode conter números")
        elif len(login) > 12:
            raise ValueError("O login deve ter no máximo 12 caracteres")
        else:
            return True 