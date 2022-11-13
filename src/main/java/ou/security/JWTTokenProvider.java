package ou.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ou.entities.User;

import java.security.KeyPair;
import java.util.*;

@Component
@PropertySource("classpath:/application.yml")
public class JWTTokenProvider {
    @Value("${authentication.tokenExpiration}")
    private Long JWT_TOKEN_EXPIRATION;

    private final KeyPair keys = Keys.keyPairFor(SignatureAlgorithm.RS512);

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_EXPIRATION);
        Map<String, Object> claimsMap = new HashMap<>();
        Long id = user.getId();
        claimsMap.put("id", id);
        claimsMap.put("role", user.getRoles().toArray()[0]);
        claimsMap.put("username", user.getUsername());
        return Jwts.builder()
                .setSubject(id.toString())
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(keys.getPrivate())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keys.getPublic())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException | SignatureException exception) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keys.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("username");
    }

    public LinkedHashMap<Object, Object> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keys.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (LinkedHashMap<Object, Object>) claims.get("role");
    }
}
