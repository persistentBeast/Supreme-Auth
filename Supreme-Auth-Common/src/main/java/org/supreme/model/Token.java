package org.supreme.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@Data
public class Token {

    String subject;
    Date expiry;
    Map<String, Object> attributes;

    public Token(Instant expiry, String subject) {
        this.expiry = Date.from(expiry);
        this.subject = subject;
    }
}
