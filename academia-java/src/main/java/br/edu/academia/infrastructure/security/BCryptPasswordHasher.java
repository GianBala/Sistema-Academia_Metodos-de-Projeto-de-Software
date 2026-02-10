package br.edu.academia.infrastructure.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordHasher implements PasswordHasher {

    @Override
    public String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    @Override
    public boolean verify(String plaintext, String hash) {
        return BCrypt.checkpw(plaintext, hash);
    }
}
