package org.supreme.tokenstore;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.supreme.model.Token;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Optional;

public class EncryptedJwtTokenStore implements TokenStore<Token>{

    private final SecretKey encKey;
    private final String audience;

    public EncryptedJwtTokenStore(SecretKey encKey, String audience) {
        this.encKey = encKey;
        this.audience = audience;
    }

    @Override
    public String create(Token token) {
        var claimsSet =
                new JWTClaimsSet.Builder()
                        .subject(token.getSubject())
                        .audience(audience)
                        .expirationTime(token.getExpiry())
                        .claim("attrs", token.getAttributes())
                        .build();
        var header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
        var jwt = new EncryptedJWT(header, claimsSet);
        try {
            var encrypter = new DirectEncrypter(encKey);
            jwt.encrypt(encrypter);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwt.serialize();
    }

    @Override
    public Optional<Token> read(String tokenId) {
        try {
            var jwt = EncryptedJWT.parse(tokenId);
            var decryptor = new DirectDecrypter(encKey);
            jwt.decrypt(decryptor);
            var claims = jwt.getJWTClaimsSet();
            if (!claims.getAudience().contains(audience)) {
                throw new JOSEException("Incorrect audience");
            }
            var expiry = claims.getExpirationTime().toInstant();
            var subject = claims.getSubject();
            var token = new Token(expiry, subject);
            var attrs = claims.getJSONObjectClaim("attrs");
            attrs.forEach((key, value) -> token.getAttributes().put(key, (String) value));
            return Optional.of(token);

        } catch (ParseException | JOSEException e) {
            return Optional.empty();
        }
    }

    @Override
    public void revoke(String tokenId) {
        throw new RuntimeException("Unimplemented method called!");
    }
}
