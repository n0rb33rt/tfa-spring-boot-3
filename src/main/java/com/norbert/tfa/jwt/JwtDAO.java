package com.norbert.tfa.jwt;


public interface JwtDAO {
    JwtToken findByToken(String token);
    void save(JwtToken jwtToken);
}
