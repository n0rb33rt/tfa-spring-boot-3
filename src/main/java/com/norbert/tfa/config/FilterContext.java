package com.norbert.tfa.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public record FilterContext(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
) {
}
