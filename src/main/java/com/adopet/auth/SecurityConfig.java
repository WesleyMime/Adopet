package com.adopet.auth;

import com.adopet.user.UserRepository;
import com.adopet.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository repository;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> {
                    cors.configurationSource(apiConfigurationSource());
                })
                .authorizeHttpRequests((requests) -> {
                    requests.requestMatchers(
                            HttpMethod.POST, "/auth/*", "/abrigos", "/tutores")
                            .permitAll();
                    requests.requestMatchers(
                            "/pets/adopt")
                            .permitAll();
                    requests.requestMatchers(
                            "/adocao", "/pets", "/pets/*")
                            .hasRole("ABRIGO");
                    requests.requestMatchers(
                                    HttpMethod.GET, "/abrigos/*")
                            .hasAnyRole("TUTOR", "ABRIGO");
                    requests.requestMatchers(
                                    "/abrigos/*")
                            .hasRole("ABRIGO");
                    requests.requestMatchers(
                        "/tutores", "/tutores/*")
                            .hasRole("TUTOR");
                    requests.anyRequest()
                            .authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((configure) -> {
                    configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(new TokenAuthenticationFilter(tokenService, repository),
                        UsernamePasswordAuthenticationFilter.class);
        ;
        return http.build();
    }

    UrlBasedCorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }
}
