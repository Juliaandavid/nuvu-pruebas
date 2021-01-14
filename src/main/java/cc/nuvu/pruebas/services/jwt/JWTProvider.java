package cc.nuvu.pruebas.services.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JWTProvider {

    @Value("${jwt.header}")
    public String header;

    @Value("${jwt.secret}")
    public String secretKey;

    @Value("${jwt.expiration}")
    public Long expiration;

    public String generateJWTToken(String subject, List<GrantedAuthority> grantedAuthorities) {
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .setSubject(String.valueOf(subject))
            .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + (expiration * 1000)))
            .compact();
    }

    public Claims getClaimsFromToken (String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}