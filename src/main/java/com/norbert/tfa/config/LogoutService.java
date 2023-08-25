package com.norbert.tfa.config;

import com.norbert.tfa.exception.ExceptionMessage;
import com.norbert.tfa.jwt.JwtToken;
import com.norbert.tfa.jwt.JwtTokenRepository;
import com.norbert.tfa.jwt.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.apache.http.entity.ContentType;

@Service
@AllArgsConstructor
public class LogoutService implements LogoutHandler {
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenService jwtTokenService;
    @SneakyThrows
    @Override
    public void logout (
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwtTokenService.isAuthHeaderValid(authHeader)){
            jwtTokenService.handleTokenException(response, ExceptionMessage.UNAUTHORIZED_ERROR_MESSAGE);
            return;
        }
        final String jwt = jwtTokenService.extractJwtTokenFromHeader(authHeader);
        JwtToken storedToken = jwtTokenRepository.findByToken(jwt).orElse(null);
        if(storedToken != null){
            storedToken.setRevoked(true);
            jwtTokenRepository.save(storedToken);
        }
    }

}
