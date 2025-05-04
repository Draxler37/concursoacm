package com.concursoacm.tools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.concursoacm.tools.security.CustomUserDetailsService;
import com.concursoacm.utils.Constantes;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/equipos/**").hasRole(Constantes.ROL_JEFE_DELEGACION)
                        .requestMatchers(HttpMethod.PUT, "/equipos/**").hasRole(Constantes.ROL_JEFE_DELEGACION)
                        .requestMatchers(HttpMethod.DELETE, "/equipos/**").hasRole(Constantes.ROL_JEFE_DELEGACION)
                        .requestMatchers(HttpMethod.PUT, "/jefes-delegacion/**").hasRole(Constantes.ROL_JEFE_DELEGACION)
                        .requestMatchers(HttpMethod.GET, "/preguntas-asignadas/equipo/**")
                        .hasRole(Constantes.ROL_JEFE_DELEGACION)
                        .anyRequest().permitAll())
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usa BCrypt para encriptar contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
