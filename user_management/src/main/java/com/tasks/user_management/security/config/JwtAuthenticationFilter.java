package com.tasks.user_management.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasks.user_management.local.models.User;
import com.tasks.user_management.local.models.UserRole;
import com.tasks.user_management.services.RefreshTokenService;
import com.tasks.user_management.utils.exceptions.TokenValidationException;
import com.tasks.user_management.utils.exceptions.UserNotFoundException;
import com.tasks.user_management.utils.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(RefreshTokenService refreshTokenService,
                                   JwtUtil jwtUtil) {
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            User user = null;
            try {
                user = refreshTokenService.getUserFromToken(token);
                jwtUtil.validateToken(token, user.getSecretToken());
                if (user.getEmail() != null) {
                    UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(user);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (UserNotFoundException e) {
                request.setAttribute("exception message", "User Not Found");
                request.setAttribute("exception status", HttpServletResponse.SC_BAD_REQUEST);
            } catch (TokenValidationException e){
                request.setAttribute("exception message", "Invalid Token");
                request.setAttribute("exception status", HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(User user) {
        List<UserRole> roles = user.getUserRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : roles) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getName());
            authorities.add(simpleGrantedAuthority);
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
    }
}
