package com.adopet.auth;

import com.adopet.LoginForm;
import com.adopet.tutor.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TutorService service;

    @PostMapping("/login")
    public String authenticate(@RequestBody @Valid LoginForm form) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.email(), form.password()));
        if (authentication.isAuthenticated())
            return tokenService.generateToken(authentication);
        return null;
    }
}
