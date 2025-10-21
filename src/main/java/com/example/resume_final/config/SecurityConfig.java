package com.example.resume_final.config;



import com.example.resume_final.config.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Security configuration:
 * - Permits public access to /api/auth/**
 * - Adds JwtFilter to validate Bearer tokens
 * - Stateless session management
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(List.of("*")); // restrict in production
                cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                cfg.setAllowedHeaders(List.of("*"));
                cors.configurationSource(request -> cfg);
            })
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/index.html", "/login.html", "/register.html", "/resume.html","/downloadResume", "/", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            );

        // place jwtFilter before UsernamePasswordAuthenticationFilter so we can set SecurityContext
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // disable default login form
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
