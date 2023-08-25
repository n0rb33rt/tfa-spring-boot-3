package com.norbert.tfa.jwt;

import com.norbert.tfa.exception.JwtTokenNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtTokenJPAService implements JwtDAO{
    private final JwtTokenRepository jwtTokenRepository;
    @Override
    public JwtToken findByToken(String token) {
        return jwtTokenRepository.findByToken(token).orElseThrow(()->new JwtTokenNotFoundException("Jwt token was not found"));
    }
    @Override
    public void save(JwtToken jwtToken) {
        jwtTokenRepository.save(jwtToken);
    }
}
