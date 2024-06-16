package org.supreme.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.supreme.model.Token;
import org.supreme.tokenstore.EncryptedJwtTokenStore;
import org.supreme.tokenstore.TokenStore;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Service
public class TokenService {

  @Value("${token.auth.jwt.encryption.key}")
  private String encryptionKey;

  @Value("${token.auth.jwt.audience}")
  private String audience;

  TokenStore<Token> tokenStore;

  @PostConstruct
  void init(){
    byte[] rawBytes = Base64.getDecoder().decode(encryptionKey);
    tokenStore =
            new EncryptedJwtTokenStore(new SecretKeySpec(rawBytes, "AES"), audience);
  }

  public boolean isTokenValid(String token, String subject) {
    Optional<Token> tokenOptional = tokenStore.read(token);
      return tokenOptional.isPresent()
              && tokenOptional.get().getExpiry().after(new Date())
              && tokenOptional.get().getSubject().equals(subject);
  }

  public String generateToken(String subject){
    //Token with 1 hour expiry
    Token newToken = new Token(subject, new Date(System.currentTimeMillis() + 60 * 60 * 1000), new HashMap<>());
    return tokenStore.create(newToken);
  }
}
