package org.supreme.tokenstore;

import java.util.Optional;

public interface TokenStore <T>{

    public String create(T token);
    public Optional<T> read(String tokenId);
    public void revoke(String tokenId);

}
