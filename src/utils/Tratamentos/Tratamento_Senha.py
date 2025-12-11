from typing import Optional

class TratamentoSenha:
    @staticmethod
    def verificar_senha(senha: str, obj : any):
        
        if len(senha) < 8 or len(senha) > 128:
            raise ValueError("A senha deve ter entre 8 e 128 caracteres.")
        elif not TratamentoSenha.verificar_tipos_caracteres(senha):
            raise ValueError("A senha deve conter pelo menos 1 letra maiúscula, 1 número e 1 caractere especial")
        elif senha == obj.nome or senha == obj.email:
            raise ValueError("A senha deve ser diferente de seu nome de usuário e email")
        else:
            return True
        
    @staticmethod
    def verificar_tipos_caracteres(senha: str):
        tipos = 0

        maiuscula = False
        minuscula = False
        numero = False
        n_alfanumero = False

        for char in senha:
            if tipos >= 3:
                break

            if char.isalpha() and char.lower() == char and minuscula == False:
                tipos += 1
                minuscula = True
            elif char.isalpha() and char.upper() == char and maiuscula == False:
                tipos += 1
                maiuscula = True
            elif char.isdigit() and numero == False:
                tipos += 1
                numero = True
            elif not char.isdigit() and not char.isalpha() and n_alfanumero == False:
                tipos += 1
                n_alfanumero = True

        if tipos >= 3:
            return True
        else:
            return False