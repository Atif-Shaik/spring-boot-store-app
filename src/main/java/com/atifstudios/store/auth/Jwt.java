package com.atifstudios.store.auth;


import com.atifstudios.store.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey key;

    public boolean isExpired() {
        return claims.getExpiration().before(Date.from(Instant.now()));
    } // method ends

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    } // method ends

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    } // method ends

    public String toString() {
        return Jwts.builder().claims(claims).signWith(key).compact();
    } // method ends


} // class ends
