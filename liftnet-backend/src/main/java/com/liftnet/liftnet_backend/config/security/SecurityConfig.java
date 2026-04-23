package com.liftnet.liftnet_backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/*
     * ✅ MODO DESARROLLO (ACTIVO)
     *
     * - NO JWT
     * - NO roles
     * - NO filtros
     * - TODO accesible
     *
     * Usar para probar negocio con Postman
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }

    /*
    ======================================================
    MODO PRODUCCIÓN (DESCOMENTAR CUANDO TODO FUNCIONE)
    ======================================================

    @Configuration
    @EnableMethodSecurity
    public static class SecurityConfigProd {

        private final JwtAuthenticationFilter jwtFilter;
        private final RateLimitingFilter rateLimitingFilter;

        public SecurityConfigProd(
                JwtAuthenticationFilter jwtFilter,
                RateLimitingFilter rateLimitingFilter) {
            this.jwtFilter = jwtFilter;
            this.rateLimitingFilter = rateLimitingFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                throws Exception {

            return http
                    // JWT → no CSRF
                    .csrf(csrf -> csrf.disable())

                    // HEADERS DE SEGURIDAD
                    .headers(headers -> headers
                            .contentTypeOptions(Customizer.withDefaults())
                            .frameOptions(frame -> frame.deny())
                            .httpStrictTransportSecurity(hsts -> hsts
                                    .includeSubDomains(true)
                                    .maxAgeInSeconds(31536000)
                            )
                    )

                    // AUTORIZACIÓN
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                    )

                    // RATE LIMITING
                    .addFilterBefore(
                            rateLimitingFilter,
                            UsernamePasswordAuthenticationFilter.class
                    )

                    // JWT FILTER
                    .addFilterBefore(
                            jwtFilter,
                            UsernamePasswordAuthenticationFilter.class
                    )

                    .build();
        }
    }
    */
}