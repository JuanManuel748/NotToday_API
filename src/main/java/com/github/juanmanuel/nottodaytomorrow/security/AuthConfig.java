package com.github.juanmanuel.nottodaytomorrow.security;

import com.github.juanmanuel.nottodaytomorrow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilt;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // Asegúrate que tu bean CorsConfigurationSource está bien configurado
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // No necesitas .authenticationProvider(authenticationProvider()) aquí si el JwtAuthenticationFilter
                // y UserDetailsService ya están configurados para trabajar juntos.
                // El JwtAuthenticationFilter debería manejar la creación del objeto Authentication a partir del token.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/test-public").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/teams/**").permitAll()
                        .requestMatchers("/tasks/**").permitAll()
                        .requestMatchers("/bills/**").permitAll()
                        .requestMatchers("/comments/**").permitAll()
                        /*
                        .requestMatchers(HttpMethod.GET, "/users").permitAll() // Permitir registro
                        .requestMatchers("/users/{id}/**").authenticated() // Proteger otros endpoints de usuario
                        /* */
                        .anyRequest().authenticated() // Proteger todos los demás endpoints
                )
                // .anonymous(anonymous -> anonymous.disable()) // Deshabilitar acceso anónimo si no lo necesitas
                .httpBasic(AbstractHttpConfigurer::disable) // Correcto, mantener deshabilitado
                .addFilterBefore(jwtAuthFilt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
