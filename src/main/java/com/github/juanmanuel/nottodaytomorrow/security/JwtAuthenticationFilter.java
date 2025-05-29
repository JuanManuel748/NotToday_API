package com.github.juanmanuel.nottodaytomorrow.security;

import com.github.juanmanuel.nottodaytomorrow.exceptions.AuthException;
import com.github.juanmanuel.nottodaytomorrow.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProv;

    @Autowired
    private UserDetailsService userDetService;

    private final RequestMatcher publicEndpoints = new OrRequestMatcher(
            new AntPathRequestMatcher("/auth/**")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //log.info("JwtAuthenticationFilter: Processing request to {}", request.getRequestURI());

        // Si la petición es para un endpoint público, saltar la lógica del filtro JWT
        if (publicEndpoints.matches(request)) {
            //log.debug("JwtAuthenticationFilter: Request to public endpoint {}, skipping JWT validation.", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);

            if (jwt != null) {
                //log.debug("JwtAuthenticationFilter: Extracted JWT from request header.");
                if (jwtTokenProv.validateToken(jwt)) {
                    //log.debug("JwtAuthenticationFilter: JWT is valid.");
                    String username = jwtTokenProv.getUsernameFromJWT(jwt);
                    //log.debug("JwtAuthenticationFilter: Username from JWT: {}", username);

                    UserDetails userDetails = userDetService.loadUserByUsername(username);
                    if (userDetails != null) {
                        //log.debug("JwtAuthenticationFilter: UserDetails loaded for {}: {}", username, userDetails.getAuthorities());
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        //log.info("JwtAuthenticationFilter: User '{}' authenticated successfully.", username);
                    } else {
                        throw new AuthException("JwtAuthenticationFilter: UserDetails not found for username: " + username);
                    }
                } else {
                    throw new AuthException("JwtAuthenticationFilter: JWT validation failed. Token: " + jwt);
                }
            } else {
                throw new AuthException("JwtAuthenticationFilter: No JWT found in request header for " + request.getRequestURI());
            }
        } catch (Exception ex) {
            // No lanzar AuthException aquí, solo loguear
            System.out.println("ERROR JWT: " + ex.getMessage());
            // Permitir que la cadena de filtros continúe
            filterChain.doFilter(request, response);
            return; // Importante: salir del método para que no se llame de nuevo a filterChain.doFilter
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}