package com.adopet.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    @Value("${adopet.jwt.secret}")
    private String secret;

    public TokenDto generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date today = new Date();
        Date dateExpiration = new Date(today.getTime() + (1000 * 60 * 60));

        return new TokenDto(Jwts.builder()
                .issuer("Adopet")
                .subject(user.getUsername())
                .issuedAt(today)
                .expiration(dateExpiration)
                .signWith(getKey())
                .compact(),
                dateExpiration.getTime(),
                user.getAuthorities().toString(),
                user.getUsername()); // TODO get from the jwt directly
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public SecretKey getKey() {
		/* To generate a new key
		SecretKey secretKey = Jwts.SIG.HS512.key().build();
        String encodedKey = Encoders.BASE64.encode(secretKey.getEncoded());
		 */
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
    }
}
