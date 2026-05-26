package com.atifstudios.store.auth;

import com.atifstudios.store.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    } // method ends

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    } // method ends

    private Jwt generateToken(User user, long tokenExpiration) {
        // this creates the token

        var claims = Jwts.claims().subject(user.getId().toString())
                        .add("email", user.getEmail())
                        .add("name", user.getName())
                        .add("role", user.getRole())
                        .issuedAt(Date.from(Instant.now()))
                        .expiration(Date.from(Instant.now().plusSeconds(tokenExpiration)))
                        .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    } // method ends

    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    } // method ends

    // this decodes the token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    } // method ends

} // class ends
