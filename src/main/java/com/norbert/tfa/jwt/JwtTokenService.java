package com.norbert.tfa.jwt;

import com.norbert.tfa.user.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final JwtDAO jwtDAO;
    @Value("${jwt_secret_key}")
    private String JWT_SECRET_KEY;
    @Value("${jwt_expiration_time}")
    private Long JWT_EXPIRATION_TIME;
    private final static Integer BEARER_PREFIX_LENGTH = 7;
    private final static String BEARER_PREFIX = "Bearer ";

    public boolean isTokenValid(String token){
        return !isTokenRevoked(token) &&  !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public boolean isAuthHeaderValid(String authHeader){
        return authHeader == null || !authHeader.startsWith(BEARER_PREFIX);
    }

    public String extractJwtTokenFromHeader(String authHeader){
        return authHeader.substring(BEARER_PREFIX_LENGTH);
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenRevoked(String token){
        return findByToken(token).isRevoked();
    }

    private JwtToken findByToken(String jwtToken){
        return jwtDAO.findByToken(jwtToken);
    }

    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetailsImpl userDetails){
        String token = generateToken(new HashMap<>(),userDetails);
        JwtToken jwtToken = JwtToken.builder()
                .token(token)
                .revoked(false)
                .user(userDetails.getUser())
                .build();
        save(jwtToken);
        return token;
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetailsImpl userDetails
    ){
        return   Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ JWT_EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public void save(JwtToken jwtToken){
        jwtDAO.save(jwtToken);
    }

    public void handleTokenException(
            HttpServletResponse response,
            String errorMessage
    ) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(errorMessage);
    }
}
