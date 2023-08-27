package com.norbert.tfa.config;

import com.norbert.tfa.exception.ExceptionMessage;
import com.norbert.tfa.jwt.JwtTokenService;
import com.norbert.tfa.user.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtTokenService.isAuthHeaderNotValid(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = jwtTokenService.extractJwtTokenFromHeader(authHeader);
        authenticateUser(jwtToken, new FilterContext(request,response,filterChain));
    }

    private void authenticateUser(String jwtToken, FilterContext context) throws ServletException, IOException {
        try {
            final String email = jwtTokenService.extractEmail(jwtToken);
            if (isUserEmailIsPresentAndNotAuthenticated(email) && jwtTokenService.isTokenValid(jwtToken))
                setAuthentication(email, context.request());
        } catch (MalformedJwtException e) {
            jwtTokenService.handleTokenException(context.response(), ExceptionMessage.TOKEN_INVALID_ERROR_MESSAGE);
        } catch (ExpiredJwtException e) {
            jwtTokenService.handleTokenException(context.response(), ExceptionMessage.TOKEN_EXPIRED_ERROR_MESSAGE);
        }
        context.filterChain().doFilter(context.request(), context.response());
    }

    private boolean isUserEmailIsPresentAndNotAuthenticated(String email) {
        return email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void setAuthentication(String email ,HttpServletRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) this.userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
