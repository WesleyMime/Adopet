package com.adopet.auth;

import com.adopet.user.UserDto;
import com.adopet.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class TokenAuthenticationFilter extends OncePerRequestFilter{

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public TokenAuthenticationFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getToken(request);

        boolean valid = tokenService.isValid(token);
        if(valid) {
            authenticate(token);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7);
    }

    private void authenticate(String token) {
        String username = tokenService.getUsername(token);
        UserDto user = userRepository.getByEmail(username);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.authorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
