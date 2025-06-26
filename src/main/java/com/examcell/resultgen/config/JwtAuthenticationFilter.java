package com.examcell.resultgen.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.examcell.resultgen.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        logger.debug("JWT Filter: Processing request to {}", request.getRequestURI());
        logger.debug("JWT Filter: Authorization header: {}", authHeader != null ? authHeader.replaceAll(".*", "[PROTECTED]") : "null"); // Mask sensitive token

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("JWT Filter: No Bearer token found or header is null. Continuing filter chain.");
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        logger.debug("JWT Filter: Extracted JWT: {}", jwt.replaceAll(".*", "[PROTECTED]")); // Mask sensitive token

        try {
            username = jwtService.extractUsername(jwt);
            logger.debug("JWT Filter: Extracted username: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                logger.debug("JWT Filter: UserDetails loaded for username: {}", userDetails.getUsername());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    logger.debug("JWT Filter: Token is valid for user: {}", userDetails.getUsername());
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("JWT Filter: SecurityContextHolder updated for user: {}", userDetails.getUsername());
                } else {
                    logger.debug("JWT Filter: Token is NOT valid for user: {}", userDetails.getUsername());
                }
            } else if (username == null) {
                logger.debug("JWT Filter: Username extracted from token is null.");
            } else {
                logger.debug("JWT Filter: User already authenticated in SecurityContext.");
            }
        } catch (Exception e) {
            logger.error("JWT Filter: Error during authentication process: {}", e.getMessage());
            // Optionally, you might want to send a specific error response here
        }

        filterChain.doFilter(request, response);
    }
}