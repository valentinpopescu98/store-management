package com.valentinpopescu.store.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentinpopescu.store.exceptions.ApiException;
import com.valentinpopescu.store.security.Roles;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    public static final String BASE_URL = "http://localhost:8080";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper mapper) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint(mapper))
                        .accessDeniedHandler(restAuthenticationDeniedHandler(mapper))
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(BASE_URL));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user").password("{noop}user123").roles(
                        Roles.USER.name()
                ).build(),
                User.withUsername("admin").password("{noop}admin123").roles(
                        Roles.USER.name(),
                        Roles.ADMIN.name()
                ).build()
        );
    }

    private AuthenticationEntryPoint restAuthenticationEntryPoint(ObjectMapper mapper) {
        return (request, response, ex) -> {
            if (response.isCommitted())
                return;

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"store\"");

            ApiException body = ApiException.of(HttpStatus.UNAUTHORIZED, "Authentication required", request.getRequestURI());
            response.getWriter().write(mapper.writeValueAsString(body));
        };
    }

    private AccessDeniedHandler restAuthenticationDeniedHandler(ObjectMapper mapper) {
        return (request, response, ex) -> {
            if (response.isCommitted())
                return;

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiException body = ApiException.of(HttpStatus.FORBIDDEN, "Access denied", request.getRequestURI());
            response.getWriter().write(mapper.writeValueAsString(body));
        };
    }
}
