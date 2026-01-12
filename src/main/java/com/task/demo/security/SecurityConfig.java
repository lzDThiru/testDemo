package com.task.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // H2 console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/favicon.ico",
                                "/runtime.*.js",
                                "/polyfills.*.js",
                                "/main.*.js",
                                "/styles.*.css",
                                "/assets/**"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // login/register
                        .requestMatchers("/h2-console/**").permitAll() // H2 console
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/tasks/**").permitAll() // TODO  should not be allowed this need to find way to only allow
                        .anyRequest().authenticated()
);



        return http.build();
    }
}
